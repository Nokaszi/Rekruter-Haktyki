package com.hactyki.ui.candidateuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hactyki.data.repositories.TestRepository

@Suppress("UNCHECKED_CAST")
class CandidateViewModelFactory(private val testRepository: TestRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CandidateViewModel(testRepository) as T
    }
}