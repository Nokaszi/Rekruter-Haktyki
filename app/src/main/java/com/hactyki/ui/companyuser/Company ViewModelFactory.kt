package com.hactyki.ui.companyuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hactyki.data.repositories.TestRepository
import com.hactyki.data.repositories.UserRepository


class CompanyViewModelFactory( private val testRepository: TestRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CompanyViewModel(testRepository) as T
    }

}