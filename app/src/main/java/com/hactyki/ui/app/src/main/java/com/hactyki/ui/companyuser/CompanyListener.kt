package com.hactyki.ui.companyuser

interface CompanyListener {
    fun onStarted()
    fun onSuccess()
    fun onFailed(message: String)
}