package ru.app.yf.data.api.json

import io.reactivex.Observable
import ru.app.yf.data.model.Video

class VideoListResponse(
    var items:MutableList<Video>,
    val totalPages:Int,
    val totalResults:Int)

