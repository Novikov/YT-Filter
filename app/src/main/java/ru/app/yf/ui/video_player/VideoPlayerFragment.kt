package ru.app.yf.ui.video_player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.fragment_video_player.*
import kotlinx.android.synthetic.main.fragment_video_player.view.*
import ru.app.yf.R
import ru.app.yf.VIDEO_URL
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.databinding.FragmentVideoPlayerBinding
import ru.app.yf.ui.IActivity


class VideoPlayerFragment : Fragment() {
    private var activityContract: IActivity? = null
    var time: Float? = null
    lateinit var youTubePlayerView: YouTubePlayerView
    lateinit var videoId: String
    lateinit var videoPLayerViewModel: VideoPlayerViewModel
    lateinit var videoPlayerRepository: VideoPlayerRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("VideoPlayerFragment", "onAttach is called")
        try {
            activityContract = context as IActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "Активити должна реализовывать интерфейс IActivityView")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("VideoPlayerFragment", "onCreate is called")


        if (arguments != null) {
            val args = VideoPlayerFragmentArgs.fromBundle(requireArguments())
            videoId = args.videoId
        } else {
            throw Exception("arguments can't be null")
        }


        val apiClient = YouTubeClient.getClient()
        videoPlayerRepository = VideoPlayerRepository(apiClient)
        videoPLayerViewModel =
            ViewModelProviders.of(this, object : ViewModelProvider.NewInstanceFactory() {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return VideoPlayerViewModel(videoPlayerRepository, videoId) as T
                }
            }).get(VideoPlayerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("VideoPlayerFragment", "onCreateView is called")
        val binding = DataBindingUtil.inflate<FragmentVideoPlayerBinding>(
            inflater,
            R.layout.fragment_video_player,
            container,
            false
        )
        val view = binding.root

        val shareImage = view.findViewById<ImageView>(R.id.shareImage)
        shareImage.setOnClickListener {
            val msg = VIDEO_URL + videoId
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, msg)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "send")
            startActivity(shareIntent)
        }


            val youTubePlayerPlaceHolder = view.findViewById(R.id.youtube_player_view) as ViewGroup
            youTubePlayerView = YouTubePlayerView(requireContext())
            youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(
                        videoId,
                        videoPLayerViewModel.videoTimeLiveData.value ?: 0f
                    )
                }
            })

            youTubePlayerView.enableBackgroundPlayback(true)
            youTubePlayerView.getPlayerUiController().showYouTubeButton(false)
            activityContract?.hideStatusBar()

            youTubePlayerView.addYouTubePlayerListener(object : YouTubePlayerListener {
                override fun onApiChange(youTubePlayer: YouTubePlayer) {

                }

                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    time = second
                }

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError
                ) {

                }

                override fun onPlaybackQualityChange(
                    youTubePlayer: YouTubePlayer,
                    playbackQuality: PlayerConstants.PlaybackQuality
                ) {

                }

                override fun onPlaybackRateChange(
                    youTubePlayer: YouTubePlayer,
                    playbackRate: PlayerConstants.PlaybackRate
                ) {

                }

                override fun onReady(youTubePlayer: YouTubePlayer) {

                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    videoPLayerViewModel.videoTimeLiveData.postValue(time)
                }

                override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {

                }

                override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {

                }

                override fun onVideoLoadedFraction(
                    youTubePlayer: YouTubePlayer,
                    loadedFraction: Float
                ) {

                }

            })

            videoPLayerViewModel.playingVideo.observe(viewLifecycleOwner, Observer {
                binding.video = it
            })

            youTubePlayerPlaceHolder.addView(youTubePlayerView)

            return view
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            Log.e("VideoPlayerFragment", "onActivityCreated is called")
        }

        override fun onStart() {
            super.onStart()
            Log.e("VideoPlayerFragment", "onStart is called")
        }

        override fun onResume() {
            super.onResume()
            Log.e("VideoPlayerFragment", "onResume is called")
        }

        override fun onPause() {
            super.onPause()
            videoPLayerViewModel.videoTimeLiveData.postValue(time)
            youTubePlayerView.release()
            Log.e("VideoPlayerFragment", "onPause is called")
        }

        override fun onStop() {
            super.onStop()
            Log.e("VideoPlayerFragment", "onStop is called")
        }

        override fun onDestroyView() {
            super.onDestroyView()
            Log.e("VideoPlayerFragment", "onDestroyView is called")

        }

        override fun onDestroy() {
            super.onDestroy()
            Log.e("VideoPlayerFragment", "onDestroy is called")

        }

        override fun onDetach() {
            super.onDetach()
            activityContract = null
            Log.e("VideoPlayerFragment", "onDetach is called")
        }

}

