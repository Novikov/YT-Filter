package ru.app.yf.ui.video_search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import ru.app.yf.R
import ru.app.yf.data.model.Video
import ru.app.yf.data.api.YouTubeClient
import ru.app.yf.data.repository.NetworkState
import ru.app.yf.ui.IActivity

class HomeFragment : Fragment() {

    private var activityContract: IActivity? = null
    lateinit var searchVideosViewModel: SearchVideosViewModel
    lateinit var adapter : SearchVideosAdapter
    lateinit var searchRecyclerView: RecyclerView
    lateinit var searchVideosRepository: SearchVideosRepository
    lateinit var navController: NavController


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

        val apiClient = YouTubeClient.getClient()
        searchVideosRepository = SearchVideosRepository(apiClient)

        searchVideosViewModel = ViewModelProviders.of(this, object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SearchVideosViewModel(searchVideosRepository) as T
            }
        }).get(SearchVideosViewModel::class.java)

        Log.e("NetworkStateOncreate",searchVideosViewModel.networkState.get()?.status.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val inputTextField = view.inputTextField as SearchEditText
        inputTextField.clearFocus()

        //input field focus
        inputTextField.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && inputTextField.isEnabled && inputTextField.isFocusable) {
                inputTextField.post(Runnable {
                    searchVideosViewModel.logoViewLiveData.postValue(false)
                    val imm =requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(inputTextField, InputMethodManager.SHOW_IMPLICIT
                    )
                })
            }
        }

        //back button pressed
        inputTextField.setOnKeyListener { v, keyCode, event ->
            Log.e("Key listener event", "Key listener event")
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Log.e("Back button pressed", "Back button pressed focus cleared")
                inputTextField.clearFocus()
                searchVideosViewModel.logoViewLiveData.postValue(true)
            true
            }
            else false
        }

        //search button pressed
        inputTextField.setOnEditorActionListener(TextView.OnEditorActionListener(function = { v, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_SEARCH
            ) {
                val query = inputTextField.text.toString()
//                searchVideosViewModel.searchRequest(query)
                val action = HomeFragmentDirections.actionHomeNavigationItemToSearchingResultFragment(query)
                navController?.navigate(action)

                true
            }
            else false
        }))


        searchVideosViewModel.logoViewLiveData.observe(viewLifecycleOwner, Observer{
            if(it){
                youtube_logo_image_view.visibility = View.VISIBLE
                clearRecyclerView()
            }
            else {
                youtube_logo_image_view.visibility = View.GONE
            }
        })

        searchVideosViewModel.searchResultsLiveData.observe(viewLifecycleOwner, Observer {
                updateRecyclerView(it)
                inputTextField.clearFocus()
        })

        searchVideosViewModel.networkState.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (searchVideosViewModel.networkState.get()!=NetworkState.WAITING){
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
                    }
                }
            }
        })

        searchRecyclerView = view.search_results_rv
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


