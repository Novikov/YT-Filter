package ru.app.yf.ui.video_search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import ru.app.yf.R
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

        return view;
    }

}