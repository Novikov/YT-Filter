package ru.app.yf.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import ru.app.yf.data.api.YouTubeClient.FIRST_PAGE
import ru.app.yf.data.api.YouTubeService
import ru.app.yf.data.model.Video

class NewVideoDataSource(private val youTubeClient : YouTubeService, private val compositeDisposable: CompositeDisposable) :
    PageKeyedDataSource<Int, Video>() {

   private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Video>
    ) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {

    }

}