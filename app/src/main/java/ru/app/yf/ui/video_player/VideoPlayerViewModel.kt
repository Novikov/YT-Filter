package ru.app.yf.ui.video_player

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import io.reactivex.disposables.CompositeDisposable
import ru.app.yf.data.model.Video
import ru.app.yf.data.repository.NetworkState

class VideoPlayerViewModel(private val videoPlayerRepository:VideoPlayerRepository, videoId: String) : ViewModel() {

    private val disposables = CompositeDisposable()
    val videoTimeLiveData = MutableLiveData<Float>()

    init {
        videoPlayerRepository.dataSourceInit(disposables)
    }

    val playingVideo: LiveData<Video> by lazy {
        videoPlayerRepository.getPlayingVideo(videoId)
    }

    val networkState : ObservableField<NetworkState> by lazy {
        videoPlayerRepository.getVideoNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}