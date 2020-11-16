package ru.app.yf.data.api

object ApiLimitCracker {
    const val API_1 = "AIzaSyDhVD3YXrCMwkx8CJEPObc7HwsOtDpQGVw"
    const val API_2 = "AIzaSyAQb1rMU7vm8NxOAvijTT3rb25Hf1KjjQE"

    val keyCollector = mutableMapOf<Int,Pair<String, Int>>()

    fun isTheQuotaOver():Boolean{
        return false
    }

    fun getApiKey(){

    }
}