package ru.app.yf.data.api

import android.util.Log
import io.reactivex.exceptions.Exceptions


object ApiLimitCracker {
   private val mapOfApiKeys = mutableMapOf(
       "AIzaSyAQb1rMU7vm8NxOAvijTT3rb25Hf1KjjQE" to true,
       "AIzaSyDhVD3YXrCMwkx8CJEPObc7HwsOtDpQGVd" to true)

    fun getSizeMapOfApiKeys(): Int {
        return mapOfApiKeys.size
    }

    fun getNextApiKey(): String? {
        var nextApiKey:String? = null
        mapOfApiKeys.forEach { (key, value) ->
            if (value) nextApiKey = key
        }

        if (nextApiKey!=null){
            mapOfApiKeys[nextApiKey!!] = false
        }
        return nextApiKey
    }

    fun getCountOfAvaliableApiKeys():Int{
        return mapOfApiKeys.filter { it.value }.count()
    }
}


