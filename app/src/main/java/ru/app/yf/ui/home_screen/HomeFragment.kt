package ru.app.yf.ui.home_screen

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
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import ru.app.yf.R
import ru.app.yf.ui.IActivity
import ru.app.yf.ui.home_screen.HomeFragmentDirections
import ru.app.yf.ui.video_search.SearchEditText

class HomeFragment : Fragment() {

    private var activityContract: IActivity? = null
    lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            activityContract = context as IActivity
        }catch (e:ClassCastException){
            throw ClassCastException(context.toString() + "Активити должна реализовывать интерфейс IActivityView")
        }
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
                    youtube_logo_image_view.visibility = View.GONE
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
                youtube_logo_image_view.visibility = View.VISIBLE
            true
            }
            else false
        }

        //search button pressed
        inputTextField.setOnEditorActionListener(TextView.OnEditorActionListener(function = { v, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_SEARCH
            ) {
                val query = inputTextField.text.toString()
                val action =
                    HomeFragmentDirections.actionHomeNavigationItemToSearchingResultFragment(query)
                navController?.navigate(action)
                activityContract?.showProgressBar()
                inputTextField.text?.clear()
                youtube_logo_image_view.visibility = View.VISIBLE
                true
            }
            else false
        }))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityContract?.showStatusBar()
        navController = Navigation.findNavController(view)
    }

    override fun onDetach() {
        super.onDetach()
        activityContract = null
    }

}


