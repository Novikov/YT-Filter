package ru.app.yf.data.model

import android.os.Parcel
import android.os.Parcelable

data class Video (var videoId:String) : Parcelable {
    lateinit var title:String
    lateinit var description:String
    lateinit var publishedDate:String
    lateinit var searchThumbNailVideoUrl: String

    lateinit var duration:String
    lateinit var viewCount:String
    lateinit var likeCount:String
    lateinit var dislikeCount:String

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoId)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(publishedDate)
        parcel.writeString(searchThumbNailVideoUrl)
        parcel.writeString(duration)
        parcel.writeString(viewCount)
        parcel.writeString(likeCount)
        parcel.writeString(dislikeCount)
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(source: Parcel): Video {
            val videoId = source.readString()
            val video = Video(videoId!!)
            video.title = source.readString()!!
            video.description = source.readString()!!
            video.publishedDate = source.readString()!!
            video.searchThumbNailVideoUrl = source.readString()!!
            video.duration = source.readString()!!
            video.viewCount = source.readString()!!
            video.likeCount = source.readString()!!
            video.dislikeCount = source.readString()!!
            return video
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }
}





