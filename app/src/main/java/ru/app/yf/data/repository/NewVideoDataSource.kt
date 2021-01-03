package ru.app.yf.data.repository

import android.util.Log
import androidx.databinding.ObservableField
import androidx.paging.PageKeyedDataSource
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.schedulers.Schedulers
import ru.app.yf.data.api.ApiLimitCracker
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.data.api.YouTubeClient.FIRST_PAGE_TOKEN
import ru.app.yf.data.api.YouTubeService
import ru.app.yf.data.api.json.SearchRequestResponse
import ru.app.yf.data.model.Video

class NewVideoDataSource(private val youTubeClient : YouTubeService, private val compositeDisposable: CompositeDisposable) :
    PageKeyedDataSource<String, Video>() {

   private var page = FIRST_PAGE_TOKEN
   private var query = YouTubeClient.QUERY

    private val _networkState  = ObservableField<NetworkState>(NetworkState.WAITING)
    val networkState: ObservableField<NetworkState>
        get() = _networkState

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Video>) {
        _networkState.set(NetworkState.LOADING)

        compositeDisposable.add(
            searchRequestWrapper(page,query)
                .doOnNext{ it.videoInitialization() }
                .subscribe({
                    callback.onResult(it.items,null,YouTubeClient.NEXT_PAGE_TOKEN)
                    networkState.set(NetworkState.LOADED)
                },{
                    networkState.set(NetworkState.ERROR)
                    Log.e("fetchVideos error", it.message )
                }))

    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Video>) {

        _networkState.set(NetworkState.LOADING)

        compositeDisposable.add(
            searchRequestWrapper(page,query)
                .doOnNext{ it.videoInitialization() }
                .subscribe({

                },{
                    networkState.set(NetworkState.ERROR)
                    Log.e("fetchVideos error", it.message )
                }))

    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Video>) {

    }

    fun SearchRequestResponse.videoInitialization(){
        compositeDisposable.add(Observable.fromIterable(items)
            .flatMap { videoInfoWrapper(it.videoId) }
            .subscribeOn(Schedulers.io())
            .toList()
            .doOnError{
                if(ApiLimitCracker.getCountOfAvaliableApiKeys()>0) {
                    Log.e(
                        "APIX",
                        "doONError searchRequestWrapper, attempts available: ${ApiLimitCracker.getCountOfAvaliableApiKeys()}"
                                + "\n changing api key..."
                    )
                    YouTubeClient.getApiKey()
                }
            }
            .retry(ApiLimitCracker.getCountOfAvaliableApiKeys().toLong())
            .subscribe({ items = it},{errorHandle(it)}))
    }

    fun searchRequestWrapper(query: String, page: String): Observable<SearchRequestResponse> {
        return Observable.defer {
            youTubeClient.searchRequest(
                YouTubeClient.URL_SNIPPET,
                YouTubeClient.VIDEOS_PER_PAGE,
                page,
                query,
                YouTubeClient.API_KEY
            )
        } .subscribeOn(Schedulers.io())
            .doOnNext { Log.e("APIX", "attempt searchRequestWrapper") }
            .doOnError{
                if(ApiLimitCracker.getCountOfAvaliableApiKeys()>0) {
                    Log.e(
                        "APIX",
                        "doONError searchRequestWrapper, attempts available: ${ApiLimitCracker.getCountOfAvaliableApiKeys()}"
                                + "\n changing api key..."
                    )
                    YouTubeClient.getApiKey()
                }
            }

            .retry(ApiLimitCracker.getCountOfAvaliableApiKeys().toLong())
    }

    fun videoInfoWrapper(videoId: String): Observable<Video> {
        return Observable.defer {
            youTubeClient.videoInfo(
                YouTubeClient.URL_SNIPPET,
                YouTubeClient.URL_STATISTICS,
                YouTubeClient.URL_CONTENT_DETAILS, videoId,
                YouTubeClient.API_KEY
            )
        }
            .subscribeOn(Schedulers.io())
            .doOnNext { Log.e("APIX", "attempt videoInfoWrapper") }
            .doOnError {
                if(ApiLimitCracker.getCountOfAvaliableApiKeys()>0){
                    Log.e("APIX","doONError videoInfoWrapper, attempts available: ${ApiLimitCracker.getCountOfAvaliableApiKeys()} "
                            + "\n changing api key...")
                    YouTubeClient.getApiKey()
                }
            }
            .retry(ApiLimitCracker.getCountOfAvaliableApiKeys().toLong())
            .map { it.item }
    }

    private fun errorHandle(it: Throwable) {
        if (it is CompositeException){
            for (ex in it.exceptions){
                ex.printStackTrace()
            }
        }

        when{
            it.message.toString().contains("HTTP 403") -> {
                _networkState.set(NetworkState.API_LIMIT_EXCEEDED)
                Log.e("NetworkState", networkState.get()?.status.toString())
            }
            it.message.toString().contains("Unable to resolve host")->{
                _networkState.set(NetworkState.NO_INTERNET)
                Log.e("NetworkState", networkState.get()?.status.toString())
            }
            it.message.toString().contains("HTTP 400") -> {
                _networkState.set(NetworkState.BAD_REQUEST)
                Log.e("NetworkState", networkState.get()?.status.toString())
            }
            else -> {
                _networkState.set(NetworkState.ERROR)
                Log.e("NetworkState", networkState.get()?.status.toString())}

        }
        _networkState.set(NetworkState.WAITING)
        Log.e("NetworkState", networkState.get()?.status.toString())
        Log.e("fetchVideos error", it.message )
        it.printStackTrace()

    }

}