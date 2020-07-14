package com.hactyki.ui.auth

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(messege:String)

}