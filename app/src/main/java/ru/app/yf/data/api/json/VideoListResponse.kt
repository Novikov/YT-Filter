package ru.app.yf.data.api.json

import ru.app.yf.data.model.Video

class VideoListResponse(val items:MutableList<Video>,
                        val totalPages:Int,
                        val totalResults:Int)