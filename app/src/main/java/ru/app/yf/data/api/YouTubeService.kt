package ru.app.yf.data.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.app.yf.data.api.json.VideoDetailResponse
import ru.app.yf.data.api.json.SearchRequestResponse


interface YouTubeService {
    @GET("search")
    fun searchRequest(@Query("part")snippet:String, @Query("maxResults")maxResults:String, @Query("pageToken")pageToken:String, @Query("q") q: String, @Query("key")apiKey:String): Observable<SearchRequestResponse>

    @GET("videos")
    fun videoInfo(@Query("part")snippet:String,@Query("part")statistics:String, @Query("part")contentDetails:String, @Query("id")id:String, @Query("key")apiKey:String): Observable<VideoDetailResponse>
}