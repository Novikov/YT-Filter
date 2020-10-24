package ru.app.yf.ui

interface IActivity {
    fun showProgressBar()
    fun hideProgressBar()
    fun hideStatusBar()
    fun showStatusBar()
    fun showErrorDialog(msg: String)
}