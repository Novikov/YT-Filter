package ru.app.yf.data.api.json

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.app.yf.data.model.Video
import java.lang.reflect.Type


class VideoInfoResponseDeserializer : JsonDeserializer<VideoItemResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): VideoItemResponse {
        val video = Video("")
        val videoResponse = VideoItemResponse(video)

        json?.asJsonObject?.entrySet()?.forEach {
            if (it.key.equals("items")) {
                Log.e("Mapper exists","items exists")
                it.value.asJsonArray.forEach {
                    //setting id
                    if (it.asJsonObject.has("id")) {
                        Log.e("Mapper videoId",it.asJsonObject.get("id").asString)
                        video.videoId = it.asJsonObject.get("id").asString
                    }

                    if (it.asJsonObject.has("snippet")) {
                        Log.e("Snippet exists","snippet exists")

                        val tmpSnippet = it.asJsonObject.getAsJsonObject("snippet")

                        if (tmpSnippet.has("publishedAt")) {
                            Log.e("Mapper publishedDate",tmpSnippet.get("publishedAt").asString)
                            var tmpPublishDate = tmpSnippet.get("publishedAt").asString
                            val endStringPosition = tmpPublishDate.indexOf('T')
                            video.publishedDate = tmpPublishDate.subSequence(0,endStringPosition).toString()
                        }

                        if (tmpSnippet.has("title")) {
                            Log.e("Mapper title",tmpSnippet.get("title").asString)
                            video.title = tmpSnippet.get("title").asString
                        }

                        if (tmpSnippet.has("description")) {
                            Log.e("Mapper description",tmpSnippet.get("description").asString)
                            video.description = tmpSnippet.get("description").asString
                        }

                        if (tmpSnippet.has("thumbnails")) {
                            val tmpThumbnails = tmpSnippet.getAsJsonObject("thumbnails")
                            if (tmpThumbnails.has("medium")) {
                                val defaultTmpThumbnail = tmpThumbnails.getAsJsonObject("medium")
                                video.searchThumbNailVideoUrl = defaultTmpThumbnail.get("url").asString
                                Log.e("Mapper thumbnailUrl",defaultTmpThumbnail.get("url").asString)
                            }
                        }
                    }

                    //setting duration
                    if (it.asJsonObject.has("contentDetails")) {
                        Log.e("contentDetails exists","contentDetails exists")

                        val tmpContentDetails = it.asJsonObject.getAsJsonObject("contentDetails")

                        if (tmpContentDetails.has("duration")) {
                            Log.e("Mapper duration",tmpContentDetails.get("duration").asString)
                            var tmpDuration = tmpContentDetails.get("duration").asString
                            val startStringPosition = tmpDuration.indexOf('T')+1
                            tmpDuration = tmpDuration.subSequence(startStringPosition,tmpDuration.length).toString()
                            tmpDuration = tmpDuration.replace('H',':')
                            tmpDuration = tmpDuration.replace('M',':')
                            tmpDuration = tmpDuration.replace('S',' ')
                            video.duration = tmpDuration
                        }
                    }

                    //setting view count, like count and dislike count
                    if (it.asJsonObject.has("statistics")) {
                        Log.e("statistics exists","statistics exists")

                        val tmpStatistics = it.asJsonObject.getAsJsonObject("statistics")

                        if (tmpStatistics.has("viewCount")) {
                            Log.e("Mapper viewCount",tmpStatistics.get("viewCount").asString)
                            val tmpViewCount = tmpStatistics.get("viewCount").asInt
                            var formatetTmpViewCount = ""
                            if(tmpViewCount>1000000){
                                formatetTmpViewCount = (tmpViewCount/1000000).toString() + "M"
                            }
                            else{
                                formatetTmpViewCount = (tmpViewCount / 10000).toString() + "K"
                            }

                            video.viewCount = "$formatetTmpViewCount Views"
                        }

                        if (tmpStatistics.has("likeCount")) {
                            Log.e("Mapper likeCount",tmpStatistics.get("likeCount").asString)
                            video.likeCount = tmpStatistics.get("likeCount").asString
                        }

                        if (tmpStatistics.has("dislikeCount")) {
                            Log.e("Mapper disLikeCount",tmpStatistics.get("dislikeCount").asString)
                            video.dislikeCount = tmpStatistics.get("dislikeCount").asString
                        }
                    }
                }
            }

        }
        videoResponse.item = video
        return videoResponse
    }
}

