package ru.app.yf.data.api

import android.app.Activity
import android.content.Context
import android.util.Log
import java.util.*


object ApiLimitCracker {
    val TAG = "APIKEY"

    val collectionOfApiKeys = arrayListOf(
        "AIzaSyDhVD3YXrCMwkx8CJEPObc7HwsOtDpQGVd",
        "AIzaSyAQb1rMU7vm8NxOAvijTT3rb25Hf1KjjQE")

    val numbersOfAttempts = collectionOfApiKeys.size.toLong()

    private var currentApiKeyIndex = 0

    fun getApiKey():String {
       var newApiKey = collectionOfApiKeys.get(currentApiKeyIndex)
        Log.e(TAG,newApiKey)
       currentApiKeyIndex++
       return newApiKey
    }
}


