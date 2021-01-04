package ru.app.yf.ui.video_search

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.app.yf.data.api.json.SearchRequestResponse
import ru.app.yf.data.repository.NetworkState

class SearchVideosViewModel(private val searchVideosRepository: SearchVideosRepository,searchRequest:String) : ViewModel() {

    private val disposables = CompositeDisposable()

    val searchRequest: LiveData<SearchRequestResponse> = searchVideosRepository.getDownloadVideosLiveData(disposables, searchRequest)

    val networkState : ObservableField<NetworkState> by lazy {
        searchVideosRepository.getVideoNetworkState()
    }

    fun newSearchRequest(searchRequest: String){
        searchVideosRepository.updateDownloadVideosLiveData(searchRequest)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}