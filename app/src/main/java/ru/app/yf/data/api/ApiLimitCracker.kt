package ru.app.yf.data.api

import android.app.Activity
import android.content.Context
import java.util.*


object ApiLimitCracker {

    private const val APP_PREFERENCES_FILE = "API_LIMIT_SHARED_PREFERENCES"

    private val collectionOfApiKeys = mutableSetOf(
        "AIzaSyDhVD3YXrCMwkx8CJEPObc7HwsOtDpQGVw",
        "AIzaSyAQb1rMU7vm8NxOAvijTT3rb25Hf1KjjQE")

    private val currentDate = Date()
    private val currentTimeMill = currentDate.time
    private val tomorrowTimeMill = currentDate.time + 86400000

    fun sharedPreferencesInitialization(activity: Activity){
        val sharedPref = activity.applicationContext.getSharedPreferences(APP_PREFERENCES_FILE,Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            collectionOfApiKeys.forEach {
                putLong(it,currentTimeMill)
            }
            apply()
        }
    }

    fun isSharedPreferencesInitializedByApiKeys(activity: Activity):Boolean{
        return activity.applicationContext.getSharedPreferences(
            APP_PREFERENCES_FILE,
            Context.MODE_PRIVATE
        ).contains(collectionOfApiKeys.first())
    }

    fun disableApiKey(api_key:String,activity: Activity){
        val sharedPref = activity.applicationContext.getSharedPreferences(APP_PREFERENCES_FILE,Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            //Assign new time(after 24 hours).
            putLong(api_key, Date().time + 86400000)
            apply()
        }
    }

    fun getWorkingKey(activity: Activity){
        val sharedPref = activity.applicationContext.getSharedPreferences(APP_PREFERENCES_FILE,Context.MODE_PRIVATE)
    }


}