package ru.app.yf.data.model

import io.reactivex.Observable
import ru.app.yf.data.model.Video

class SearchRequestResponse
    (var items:MutableList<Video>,
    val totalPages:Int,
    val totalResults:Int,
    val nextPageToken:String)

