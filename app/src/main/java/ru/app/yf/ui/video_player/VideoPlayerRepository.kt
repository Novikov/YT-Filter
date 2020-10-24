package ru.app.yf.ui.video_player

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import io.reactivex.disposables.CompositeDisposable
import ru.app.yf.data.api.YouTubeService
import ru.app.yf.data.model.Video
import ru.app.yf.data.repository.NetworkState
import ru.app.yf.data.repository.VideoDataSource

class VideoPlayerRepository (private val youTubeClient: YouTubeService) {
    lateinit var videoDataSource: VideoDataSource

    fun getVideoNetworkState(): ObservableField<NetworkState> {
        return videoDataSource.networkState
    }

    fun getPlayingVideo(videoId:String):LiveData<Video>{
        videoDataSource.getPlayingVideo(videoId)
        return videoDataSource.playingVideoResponse
    }

    fun dataSourceInit(compositeDisposable: CompositeDisposable){
        videoDataSource = VideoDataSource(youTubeClient,compositeDisposable)
    }
}