package ru.app.yf.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import ru.app.yf.R
import ru.app.yf.ui.home_screen.HomeFragmentDirections


class MainActivity : AppCompatActivity(), IActivity {

    lateinit var mProgress: ProgressBar
    lateinit var fragmentManager:FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        mProgress = findViewById(R.id.progressBar)
        fragmentManager = supportFragmentManager

//        /**ActionBar and BottomNavigationBar are temporary disabled*/
//         val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//         val navController = findNavController(R.id.nav_host_fragment)
//
//         val appBarConfiguration = AppBarConfiguration(setOf(
//         R.id.home_navigation_item,
//         R.id.liked_navigation_item,
//         R.id.watch_later_navigation_item))
//
//         //BottomNavigationBar setup
//         bottomNavigationView.setupWithNavController(navController)it
        Log.e("Intent",intent.action)
        if (intent.action== Intent.ACTION_VIEW){
            intent.data.let {
                if(intent.data.toString().contains("http")){
                    val videoUrl = intent.dataString
                    val videoId = if (videoUrl!!.contains("=")){
                        val videoIdCharPosition = videoUrl!!.indexOf("=") + 1
                        videoUrl.subSequence(videoIdCharPosition, videoUrl.length).toString()
                    } else{
                        val videoIdCharPosition = videoUrl!!.indexOf("e") + 2
                        videoUrl.subSequence(videoIdCharPosition, videoUrl.length).toString()
                    }
                    val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                    val action = HomeFragmentDirections.actionHomeNavigationItemToVideoPlayerFragment(videoId)
                    navController.navigate(action)
                }
            }
        }
    }

    override fun showProgressBar() {
        Log.e("ProgressBar", "Loading")
        mProgress.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressBar() {
        Log.e("ProgressBar", "Hide")
        mProgress.visibility = ProgressBar.INVISIBLE
    }

    override fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    override fun showStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        actionBar?.show()
    }


    override fun showErrorDialog(msg: String) {
        try {
            Log.e("Hash", this.hashCode().toString())
            ErrorDialogFragment.newInstance(msg).show(fragmentManager, "ErrorDialogFragment")
        }
        catch (ex: Exception){
            Log.e("SearchActivity", ex.message)
        }
    }
}