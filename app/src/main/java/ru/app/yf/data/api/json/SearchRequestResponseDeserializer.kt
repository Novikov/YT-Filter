package ru.app.yf.data.api.json

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.data.model.SearchRequestResponse
import ru.app.yf.data.model.Video
import java.lang.reflect.Type

class SearchRequestResponseDeserializer : JsonDeserializer<SearchRequestResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SearchRequestResponse {

        var videoList = mutableListOf<Video>()
        var totalPages:Int = 0
        var totalResults:Int = 0
        var nextPageToken:String = ""

        //getting page info
        json?.asJsonObject?.entrySet()?.forEach {
            if (it.key.equals("nextPageToken")){
                nextPageToken = it.value.asString
            }

            if (it.key.equals("pageInfo")){
                var tmpPageInfo = it.value.asJsonObject

                if (tmpPageInfo.has("totalResults")){
                    val tpmTotalResults = tmpPageInfo.get("totalResults").asInt
                    totalResults = tpmTotalResults
                }

                if (tmpPageInfo.has("resultsPerPage")){
                    if (totalResults>0){
                        totalPages = totalResults/tmpPageInfo.get("resultsPerPage").asInt
                    }
                }
            }
        }


        //getting videos id
        json?.asJsonObject?.entrySet()?.forEach {
           //inside items array
            if(it.key.equals("items")){
                //is has id item?
                it.value.asJsonArray.forEach {
                    if(it.asJsonObject.has("id")){
                        var tmpId = it.asJsonObject.getAsJsonObject("id")
                        //is has kind item
                        if (tmpId.has("kind")) {
                            if (tmpId.get("kind").asString == "youtube#video") {
                                //is has videoId item
                                if (tmpId.has("videoId")) {
                                    val video = Video(tmpId.get("videoId").asString)
                                    videoList.add(video)
                                }
                            }
                        }
                    }
                }
            }
        }

        val videoListResponse  =
            SearchRequestResponse(videoList,totalPages,totalResults,nextPageToken)

//        Log.e("Serialization", "Total pages : $totalPages, total results : $totalResults,next page token - ${YouTubeClient.NEXT_PAGE_TOKEN} videolist: ${videoList}")

        return videoListResponse
    }
}