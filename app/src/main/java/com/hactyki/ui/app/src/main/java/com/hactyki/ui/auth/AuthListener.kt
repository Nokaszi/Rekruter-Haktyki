package com.hactyki.ui.auth

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(massage: String)

}