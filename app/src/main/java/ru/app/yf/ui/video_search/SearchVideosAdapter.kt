package ru.app.yf.ui.video_search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.app.yf.R
import ru.app.yf.data.model.Video
import ru.app.yf.databinding.SearchVideoViewHolderBinding

class SearchVideosAdapter(val videos:MutableList<Video>) : RecyclerView.Adapter<SearchVideosAdapter.SearchViewHolder>() {
    var navController:NavController? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<SearchVideoViewHolderBinding>(inflater, R.layout.search_video_view_holder,parent,false)
        navController = Navigation.findNavController(parent)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val video = videos[position]
        holder.binding.video = video
        holder.itemView.setOnClickListener {
            val action = SearchingResultFragmentDirections.actionSearchingResultFragmentToVideoPlayerFragment(video.videoId)
            navController?.navigate(action)
        }
        holder.bind(video)
    }

    fun setMovies(movieList: List<Video>) {
        this.videos.clear()
        this.videos.addAll(movieList)
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(val binding: SearchVideoViewHolderBinding) : RecyclerView.ViewHolder(binding.root){
        val youTubeImageView: ImageView = itemView.findViewById<ImageView>(R.id.youTubeThumbNailImageView)

        fun bind(video: Video){
            Picasso
                .get()
                .load(video.searchThumbNailVideoUrl)
                .fit()
                .into(youTubeImageView);
        }
    }
}