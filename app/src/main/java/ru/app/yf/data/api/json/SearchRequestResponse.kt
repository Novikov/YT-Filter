package ru.app.yf.data.api.json

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.app.yf.data.api.ApiLimitCracker
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.data.api.YouTubeService
import ru.app.yf.data.model.Video
import ru.app.yf.data.repository.NetworkState

class SearchRequestResponse
    (var items:List<Video>,
    val totalPages:Int,
    val totalResults:Int,
    val nextPageToken:String)


