package ru.app.yf.data.api

import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import ru.app.yf.data.api.json.SearchRequestResponseDeserializer
import ru.app.yf.data.api.json.VideoInfoResponseDeserializer
import ru.app.yf.data.api.json.VideoItemResponse
import ru.app.yf.data.api.json.VideoListResponse
import java.util.concurrent.TimeUnit


interface YouTubeService {
    @GET("search")
    fun searchRequest(@Query("part")snippet:String, @Query("maxResults")maxResults:String, @Query("pageToken")pageToken:String, @Query("q") q: String, @Query("key")apiKey:String): Observable<VideoListResponse>

    @GET("videos")
    fun videoInfo(@Query("part")snippet:String,@Query("part")statistics:String, @Query("part")contentDetails:String, @Query("id")id:String, @Query("key")apiKey:String): Observable<VideoItemResponse>
}