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

    fun getPlayingVideo(compositeDisposable: CompositeDisposable, videoId:String):LiveData<Video>{
        videoDataSource = VideoDataSource(youTubeClient,compositeDisposable)
        videoDataSource.getPlayingVideo(videoId)
        return videoDataSource.playingVideoResponse
    }

    fun getVideoNetworkState(): ObservableField<NetworkState> {
        return videoDataSource.networkState
    }
}