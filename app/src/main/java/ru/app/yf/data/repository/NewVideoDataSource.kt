package ru.app.yf.data.repository

import android.util.Log
import androidx.databinding.ObservableField
import androidx.paging.PageKeyedDataSource
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.schedulers.Schedulers
import ru.app.yf.data.api.ApiLimitCracker
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.data.api.YouTubeClient.FIRST_PAGE_TOKEN
import ru.app.yf.data.api.YouTubeService
import ru.app.yf.data.api.json.SearchRequestResponse
import ru.app.yf.data.model.Video

class NewVideoDataSource(private val youTubeClient : YouTubeService, private val compositeDisposable: CompositeDisposable) :
    PageKeyedDataSource<String, Video>() {

   private var page = FIRST_PAGE_TOKEN
   private var query = YouTubeClient.QUERY

    private val _networkState  = ObservableField<NetworkState>(NetworkState.WAITING)
    val networkState: ObservableField<NetworkState>
        get() = _networkState

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Video>) {

    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Video>) {

    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Video>) {

    }



}