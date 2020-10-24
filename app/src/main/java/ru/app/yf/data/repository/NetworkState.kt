package ru.app.yf.data.repository

enum class Status {
    WAITING,
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {

    companion object {
        val WAITING:NetworkState
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val API_LIMIT_EXCEEDED: NetworkState
        val NO_INTERNET:NetworkState

        init {
            WAITING = NetworkState(Status.WAITING,"Waiting user request")
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong")
            API_LIMIT_EXCEEDED = NetworkState(Status.FAILED, "API limit exceeded")
            NO_INTERNET = NetworkState(Status.FAILED,"No internet connection")
        }
    }
}