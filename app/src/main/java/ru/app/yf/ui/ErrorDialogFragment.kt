package ru.app.yf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_error.view.*
import ru.app.yf.R

class ErrorDialogFragment : DialogFragment() {
    companion object {
        private val ERROR_DIALOG_FRAGMET = "app_error_dialog_fragment"

        fun newInstance(msg:String): ErrorDialogFragment {
            val args = Bundle()
            args.putString(ERROR_DIALOG_FRAGMET,msg)
            val fragment = ErrorDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var sMsg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments!=null && requireArguments().containsKey(ERROR_DIALOG_FRAGMET)){
            sMsg = requireArguments().getString(ERROR_DIALOG_FRAGMET)
        }
        else {
            throw IllegalArgumentException("Error message can't be empty")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.dialog_error, container, false)

        val errorTextView = view.errorMessageTextView as TextView
        errorTextView.text = sMsg

        val okButton = view.ok_error_button as Button
        okButton.setOnClickListener(View.OnClickListener {
            this.dismiss()
        })

        return view
    }

}