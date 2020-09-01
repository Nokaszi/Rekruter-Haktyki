package com.hactyki.ui.candidateuser

interface CandidateListener {
    fun onStarted()
    fun onSuccess()
    fun onFailed(message: String)
}