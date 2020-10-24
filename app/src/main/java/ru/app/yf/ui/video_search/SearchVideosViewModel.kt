package ru.app.yf.ui.video_search

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.app.yf.data.model.Video
import ru.app.yf.data.repository.NetworkState

class SearchVideosViewModel(private val searchVideosRepository: SearchVideosRepository) : ViewModel() {

    private val disposables = CompositeDisposable()
    val logoViewLiveData = MutableLiveData<Boolean>()

    val searchResultsLiveData : MutableLiveData<MutableList<Video>> by lazy {
        searchVideosRepository.getDownloadVideosLiveData()
    }

    val networkState : ObservableField<NetworkState> by lazy {
        searchVideosRepository.getVideoNetworkState()
    }

    init {
        searchVideosRepository.dataSourceInit(disposables)
    }

    fun searchRequest(query:String) {
        searchVideosRepository.initSearchResult(query)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}