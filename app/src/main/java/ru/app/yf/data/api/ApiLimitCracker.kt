package ru.app.yf.data.api

import android.app.Activity
import android.content.Context
import java.util.*


object ApiLimitCracker {

    private val collectionOfApiKeys = mutableListOf(
        "AIzaSyDhVD3YXrCMwkx8CJEPObc7HwsOtDpQGVw",
        "AIzaSyAQb1rMU7vm8NxOAvijTT3rb25Hf1KjjQE")

    private var currentApiKeyIndex = 0

    fun getApiKey():String{
       var newApiKey = collectionOfApiKeys.get(currentApiKeyIndex)
       currentApiKeyIndex++
       return newApiKey
    }
}


