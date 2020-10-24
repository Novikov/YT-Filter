package ru.app.yf.data.api.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.app.yf.data.model.Video
import java.lang.reflect.Type

class SearchRequestResponseDeserializer : JsonDeserializer<VideoListResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): VideoListResponse {
        val videoListResponse =
            VideoListResponse(
                mutableListOf()
            )

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
                                    videoListResponse.items.add(video)
                                }
                            }
                        }
                    }
                }
            }
        }

        return videoListResponse
    }
}