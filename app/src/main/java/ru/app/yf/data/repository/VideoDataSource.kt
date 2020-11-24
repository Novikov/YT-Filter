package ru.app.yf.data.repository

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.app.yf.data.api.ApiLimitCracker
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.data.api.YouTubeService
import ru.app.yf.data.model.Video

class VideoDataSource (private val youTubeClient : YouTubeService, private val compositeDisposable: CompositeDisposable) {
    private val _networkState  = ObservableField<NetworkState>(NetworkState.WAITING)
    val networkState: ObservableField<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val downloadedVideosList = MutableLiveData<MutableList<Video>>()
    val downloadedVideosListResponse:MutableLiveData<MutableList<Video>>
        get() = downloadedVideosList

    private val playingVideo = MutableLiveData<Video>()
    val playingVideoResponse : LiveData<Video>
        get() = playingVideo


    fun fetchVideos(query:String) {
        _networkState.set(NetworkState.LOADING)
        Log.e("NetworkState", networkState.get()?.status.toString())

        try {
            compositeDisposable.add(
                searchRequestWrapper(query)
                    .doOnError { YouTubeClient.getApiKey() }
                    .retry(ApiLimitCracker.numbersOfApiKeys)
                .flatMapIterable {it}
                .flatMap { video -> videoInfoWrapper(video.videoId)
                    .subscribeOn(Schedulers.io())
                }
                .toList()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.e("new videosId",it.toString())
                    downloadedVideosList.postValue(it)
                    _networkState.set(NetworkState.LOADED)
                    Log.e("NetworkState", networkState.get()?.status.toString())
                    _networkState.set(NetworkState.WAITING)
                    Log.e("NetworkState", networkState.get()?.status.toString())

                },{
                    errorHandle(it)
                }))

        }
        catch (e: Exception){
            Log.e("fetchVideos",e.message)
        }
    }

    fun getPlayingVideo(videoId: String){
        _networkState.set(NetworkState.LOADING)
        Log.e("NetworkState", networkState.get()?.status.toString())

        try{
            compositeDisposable.add(
                videoInfoWrapper(videoId)
                    .doOnError { YouTubeClient.getApiKey() }
                    .retry(ApiLimitCracker.numbersOfApiKeys)
                    .subscribeOn(Schedulers.io())
                    .subscribe ({
                        Log.e("playingVideo",it.toString())
                        playingVideo.postValue(it)
                        _networkState.set(NetworkState.LOADED)
                        Log.e("NetworkState", networkState.get()?.status.toString())
                        _networkState.set(NetworkState.WAITING)
                        Log.e("NetworkState", networkState.get()?.status.toString())
                    },{
                        errorHandle(it)
                    })
            )
        }
        catch (e: Exception){
            Log.e("fetchVideos",e.message)
        }
    }

     fun searchRequestWrapper(query: String): Observable<MutableList<Video>> {
        return youTubeClient.searchRequest(
            YouTubeClient.URL_SNIPPET,
            YouTubeClient.MAX_RESULT, query,
            YouTubeClient.API_KEY
        ).map {it.items}

    }

    private fun errorHandle(it: Throwable) {
        when{
            it.message.toString().contains("HTTP 403") -> {
                _networkState.set(NetworkState.API_LIMIT_EXCEEDED)
                Log.e("NetworkState", networkState.get()?.status.toString())
            }
            it.message.toString().contains("Unable to resolve host")->{
                _networkState.set(NetworkState.NO_INTERNET)
                Log.e("NetworkState", networkState.get()?.status.toString())
            }
            else -> {_networkState.set(NetworkState.ERROR)
            Log.e("NetworkState", networkState.get()?.status.toString())}

        }
        _networkState.set(NetworkState.WAITING)
        Log.e("NetworkState", networkState.get()?.status.toString())
        Log.e("fetchVideos error", it.message)
    }

     fun videoInfoWrapper(videoId: String): Observable<Video> {
        return youTubeClient.videoInfo(
            YouTubeClient.URL_SNIPPET,
            YouTubeClient.URL_STATISTICS,
            YouTubeClient.URL_CONTENT_DETAILS, videoId,
            YouTubeClient.API_KEY
        ).map {it.item}
            .doOnError{
                errorHandle(it)
            }
    }
}