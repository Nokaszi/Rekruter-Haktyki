package com.hactyki.ui.companyuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hactyki.R
import com.hactyki.databinding.ActivityNewTestBinding
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class NewTestActivity : AppCompatActivity(),DIAware {

    override val di by di()
    private val factory:CompanyViewModelFactory by instance()
    private lateinit var viewModel: CompanyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_test)

        val binding:ActivityNewTestBinding=DataBindingUtil.setContentView(this,R.layout.activity_new_test)
        viewModel= ViewModelProvider(this,factory).get(CompanyViewModel::class.java)
        binding.viewmodel=viewModel
    }
}
