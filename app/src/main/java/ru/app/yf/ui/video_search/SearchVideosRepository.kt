package ru.app.yf.ui.video_search

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import ru.app.yf.data.api.YouTubeService
import ru.app.yf.data.model.Video
import ru.app.yf.data.repository.NetworkState
import ru.app.yf.data.repository.VideoDataSource

class SearchVideosRepository(private val youTubeClient:YouTubeService) {

    lateinit var videoDataSource: VideoDataSource

    fun getDownloadVideosLiveData(compositeDisposable: CompositeDisposable,searchRequest:String): MutableLiveData<MutableList<Video>>{
        videoDataSource = VideoDataSource(youTubeClient,compositeDisposable)
        videoDataSource.fetchVideos(searchRequest)
        return videoDataSource.downloadedVideosListResponse
    }

    fun getVideoNetworkState(): ObservableField<NetworkState> {
        return videoDataSource.networkState
    }

    fun updateDownloadVideosLiveData(searchRequest:String){
        videoDataSource.fetchVideos(searchRequest)
    }

}