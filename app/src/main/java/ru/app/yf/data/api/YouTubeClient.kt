package ru.app.yf.data.api

import android.util.Log
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.app.yf.data.api.json.SearchRequestResponseDeserializer
import ru.app.yf.data.api.json.VideoInfoResponseDeserializer
import ru.app.yf.data.api.json.VideoItemResponse
import ru.app.yf.data.api.json.VideoListResponse
import ru.app.yf.data.model.Video
import java.util.concurrent.TimeUnit

object YouTubeClient {
        private const val YOUTUBE_BASE_URL="https://www.googleapis.com/youtube/v3/"
        lateinit var API_KEY:String
        const val MAX_RESULT = "15"
        const val URL_SNIPPET = "snippet"
        const val URL_STATISTICS = "statistics"
        const val URL_CONTENT_DETAILS = "contentDetails"

        init {
                Log.e("APIX","getNextApiKey inner")
                getApiKey()
        }

        fun getApiKey(){
                API_KEY = ApiLimitCracker.getNextApiKey()?:throw Exception("The keys are out")
        }

        fun getClient():YouTubeService{

                        val logging = HttpLoggingInterceptor()
                        logging.level = HttpLoggingInterceptor.Level.BODY

                        val okHttpClient =  OkHttpClient.Builder()
                                .addInterceptor(logging)
                                .readTimeout(20, TimeUnit.SECONDS)
                                .build()

                        val gson = GsonBuilder()
                                .registerTypeAdapter(
                                        VideoListResponse::class.java,
                                        SearchRequestResponseDeserializer()
                                )
                                .registerTypeAdapter(
                                        VideoItemResponse::class.java,
                                        VideoInfoResponseDeserializer()
                                )
                                .create()

                        val retrofit = Retrofit.Builder()
                                .baseUrl(YOUTUBE_BASE_URL)
                                .client(okHttpClient)
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build()

                        return retrofit.create(YouTubeService::class.java)
                }

}



