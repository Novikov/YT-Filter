package ru.app.yf.ui.video_search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_searching_result.*
import kotlinx.android.synthetic.main.fragment_searching_result.view.*
import ru.app.yf.R
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.data.model.Video
import ru.app.yf.data.repository.NetworkState
import ru.app.yf.ui.IActivity

class SearchingResultFragment : Fragment() {

    private var activityContract: IActivity? = null
    lateinit var searchVideosViewModel: SearchVideosViewModel
    lateinit var adapter : SearchVideosAdapter
    lateinit var searchRecyclerView: RecyclerView
    lateinit var searchVideosRepository: SearchVideosRepository
    lateinit var navController: NavController
    lateinit var searchRequest:String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            activityContract = context as IActivity
        }catch (e:ClassCastException){
            throw ClassCastException(context.toString() + "Активити должна реализовывать интерфейс IActivityView")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Getting search request string from navController
        val args = SearchingResultFragmentArgs.fromBundle(requireArguments())
        searchRequest = args.SearchRequest

        val apiClient = YouTubeClient.getClient()
        searchVideosRepository = SearchVideosRepository(apiClient)

        searchVideosViewModel = ViewModelProviders.of(this, object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SearchVideosViewModel(searchVideosRepository,searchRequest) as T
            }
        }).get(SearchVideosViewModel::class.java)

        Log.e("NetworkStateOncreate",searchVideosViewModel.networkState.get()?.status.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_searching_result, container, false)

        //Setting search request string to EditText field
        val editText = view.findViewById<SearchEditText>(R.id.searchEditText)
        editText.setText(searchRequest, TextView.BufferType.EDITABLE)

        searchVideosViewModel.searchResultsLiveData.observe(viewLifecycleOwner, Observer {
            updateRecyclerView(it)
            searchEditText.clearFocus()
        })

        searchVideosViewModel.networkState.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (searchVideosViewModel.networkState.get()!= NetworkState.WAITING){
                    when(searchVideosViewModel.networkState.get()){
                        NetworkState.LOADING -> {
                            activityContract?.showProgressBar()
                        }
                        NetworkState.LOADED -> {
                            activityContract?.hideProgressBar()
                        }
                        NetworkState.NO_INTERNET -> {
                            activityContract?.hideProgressBar()
                            activityContract?.showErrorDialog(NetworkState.NO_INTERNET.msg)
                        }
                        NetworkState.ERROR -> {
                            activityContract?.hideProgressBar()
                            activityContract?.showErrorDialog(NetworkState.ERROR.msg)
                        }
                        NetworkState.API_LIMIT_EXCEEDED -> {
                            activityContract?.hideProgressBar()
                            activityContract?.showErrorDialog(NetworkState.API_LIMIT_EXCEEDED.msg)
                        }
                        NetworkState.BAD_REQUEST -> {
                            activityContract?.hideProgressBar()
                            activityContract?.showErrorDialog(NetworkState.BAD_REQUEST.msg)
                        }
                    }
                }
            }
        })

        searchRecyclerView = view.search_results_rv1
        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SearchVideosAdapter(
            mutableListOf()
        )
        searchRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityContract?.showStatusBar()
        navController = Navigation.findNavController(view)

        //search button pressed
        searchEditText.setOnEditorActionListener(TextView.OnEditorActionListener(function = { v, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_SEARCH
            ) {
                val query = searchEditText.text.toString()
                YouTubeClient.QUERY = query
                searchVideosViewModel.newSearchRequest(query)
                true
            }
            else false
        }))
    }


    fun updateRecyclerView(movieList: List<Video>) {
        adapter.videos.clear()
        adapter.videos.addAll(movieList)
        adapter.notifyDataSetChanged()
    }

    fun clearRecyclerView(){
        adapter.videos.clear()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
        activityContract = null
    }

}