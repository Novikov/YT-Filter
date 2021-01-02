package ru.app.yf.data.repository

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.app.yf.data.api.ApiLimitCracker
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.data.api.YouTubeClient.FIRST_PAGE_TOKEN
import ru.app.yf.data.api.YouTubeService
import ru.app.yf.data.model.Video

class NewVideoDataSource(private val youTubeClient : YouTubeService, private val compositeDisposable: CompositeDisposable) :
    PageKeyedDataSource<Int, Video>() {

   private var page = FIRST_PAGE_TOKEN

    private val _networkState  = ObservableField<NetworkState>(NetworkState.WAITING)
    val networkState: ObservableField<NetworkState>
        get() = _networkState

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Video>) {
        _networkState.set(NetworkState.LOADING)

//        compositeDisposable.add(
//            searchRequestWrapper(page)
//                .flatMapIterable {it}
//                .flatMap { video -> videoInfoWrapper(video.videoId)
//                    .subscribeOn(Schedulers.io())
//                }
//                .toList()
//                .subscribe({
//                    Log.e("new videosId",it.toString())
//                    downloadedVideosList.postValue(it)
//                    _networkState.set(NetworkState.LOADED)
//                    Log.e("NetworkState", networkState.get()?.status.toString())
//                    _networkState.set(NetworkState.WAITING)
//                    Log.e("NetworkState", networkState.get()?.status.toString())
//
//                })

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {

    }



}