package ru.app.yf.data.repository
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.schedulers.Schedulers
import ru.app.yf.data.api.ApiLimitCracker
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.data.api.YouTubeService
import ru.app.yf.data.api.json.SearchRequestResponse
import ru.app.yf.data.model.Video
class VideoDataSource(
    private val youTubeClient: YouTubeService,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = ObservableField<NetworkState>(NetworkState.WAITING)
    val networkState: ObservableField<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate
    private val search = MutableLiveData<SearchRequestResponse>()
    val searchResponse: LiveData<SearchRequestResponse>
        get() = search
    private val playingVideo = MutableLiveData<Video>()
    val playingVideoResponse: LiveData<Video>
        get() = playingVideo

    fun fetchVideoPreviews(query: String) {
        _networkState.set(NetworkState.LOADING)
        Log.e("NetworkState", networkState.get()?.status.toString())
        try {
            compositeDisposable.add(Observable.defer {
                youTubeClient.searchRequest(
                    YouTubeClient.URL_SNIPPET,
                    YouTubeClient.VIDEOS_PER_PAGE,
                    YouTubeClient.FIRST_PAGE_TOKEN,
                    query,
                    YouTubeClient.API_KEY
                )
            }.subscribeOn(Schedulers.io())
                .doOnNext { Log.e("APIX", "attempt searchRequestWrapper") }
                .doOnError {
                    if (ApiLimitCracker.getCountOfAvaliableApiKeys() > 0) {
                        Log.e(
                            "APIX",
                            "doONError searchRequestWrapper, attempts available: ${ApiLimitCracker.getCountOfAvaliableApiKeys()}"
                                    + "\n changing api key..."
                        )
                        YouTubeClient.getApiKey()
                    }
                }
                .retry(ApiLimitCracker.getCountOfAvaliableApiKeys().toLong())
                .subscribe ({ videosPreviewContentInit(it)},{errorHandle(it)}))
        } catch (e: Exception) {
            Log.e("fetchVideos", e.message)
        }
    }
    private fun videosPreviewContentInit(searchRequestResponse: SearchRequestResponse) {
        try {
            compositeDisposable.add(
                Observable.fromIterable(searchRequestResponse.items)
                    .flatMap { Observable.defer {
                        youTubeClient.videoInfo(
                            YouTubeClient.URL_SNIPPET,
                            YouTubeClient.URL_STATISTICS,
                            YouTubeClient.URL_CONTENT_DETAILS,
                            it.videoId,
                            YouTubeClient.API_KEY
                        )
                    }
                        .subscribeOn(Schedulers.io())
                        .doOnNext { Log.e("APIX", "attempt videoInfoWrapper") }
                        .doOnError {
                            if (ApiLimitCracker.getCountOfAvaliableApiKeys() > 0) {
                                Log.e(
                                    "APIX",
                                    "doONError videoInfoWrapper, attempts available: ${ApiLimitCracker.getCountOfAvaliableApiKeys()} "
                                            + "\n changing api key..."
                                )
                                YouTubeClient.getApiKey()
                            }
                        }
                        .retry(ApiLimitCracker.getCountOfAvaliableApiKeys().toLong())
                        .map { it.item } }
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        searchRequestResponse.items = it
                        Log.e("new videosId",searchRequestResponse.items.toString())
                        search.postValue(searchRequestResponse)
                        _networkState.set(NetworkState.LOADED)
                        Log.e("NetworkState", networkState.get()?.status.toString())
                        _networkState.set(NetworkState.WAITING)
                        Log.e("NetworkState", networkState.get()?.status.toString())
                    }, {
                        errorHandle(it)
                    })
            )
        } catch (e: Exception) {
            Log.e("fetchVideos", e.message)
        }
    }
    fun videoPlayerContentInit(videoId: String) {
        _networkState.set(NetworkState.LOADING)
        Log.e("NetworkState", networkState.get()?.status.toString())
        try {
            compositeDisposable.add(
                Observable.defer {
                    youTubeClient.videoInfo(
                        YouTubeClient.URL_SNIPPET,
                        YouTubeClient.URL_STATISTICS,
                        YouTubeClient.URL_CONTENT_DETAILS,
                        videoId,
                        YouTubeClient.API_KEY
                    )
                }
                    .subscribeOn(Schedulers.io())
                    .doOnNext { Log.e("APIX", "attempt videoInfoWrapper") }
                    .doOnError {
                        if (ApiLimitCracker.getCountOfAvaliableApiKeys() > 0) {
                            Log.e(
                                "APIX",
                                "doONError videoInfoWrapper, attempts available: ${ApiLimitCracker.getCountOfAvaliableApiKeys()} "
                                        + "\n changing api key..."
                            )
                            YouTubeClient.getApiKey()
                        }
                    }
                    .retry(ApiLimitCracker.getCountOfAvaliableApiKeys().toLong())
                    .map { it.item }
                    .subscribe({
                        Log.e("playingVideo", it.toString())
                        playingVideo.postValue(it)
                        _networkState.set(NetworkState.LOADED)
                        Log.e("NetworkState", networkState.get()?.status.toString())
                        _networkState.set(NetworkState.WAITING)
                        Log.e("NetworkState", networkState.get()?.status.toString())
                    }, {
                        errorHandle(it)
                    })
            )
        } catch (e: Exception) {
            Log.e("fetchVideos", e.message)
        }
    }
    private fun errorHandle(it: Throwable) {
        if (it is CompositeException) {
            for (ex in it.exceptions) {
                ex.printStackTrace()
            }
        }
        when {
            it.message.toString().contains("HTTP 403") -> {
                _networkState.set(NetworkState.API_LIMIT_EXCEEDED)
                Log.e("NetworkState", networkState.get()?.status.toString())
            }
            it.message.toString().contains("Unable to resolve host") -> {
                _networkState.set(NetworkState.NO_INTERNET)
                Log.e("NetworkState", networkState.get()?.status.toString())
            }
            it.message.toString().contains("HTTP 400") -> {
                _networkState.set(NetworkState.BAD_REQUEST)
                Log.e("NetworkState", networkState.get()?.status.toString())
            }
            else -> {
                _networkState.set(NetworkState.ERROR)
                Log.e("NetworkState", networkState.get()?.status.toString())
            }
        }
        _networkState.set(NetworkState.WAITING)
        Log.e("NetworkState", networkState.get()?.status.toString())
        Log.e("fetchVideos error", it.message)
        it.printStackTrace()
    }
}