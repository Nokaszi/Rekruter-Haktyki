package com.hactyki.ui.companyuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hactyki.R
import com.hactyki.classes.Test
import com.hactyki.databinding.ActivityPropertiesTestBinding
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class PropertiesTestActivity : AppCompatActivity(), DIAware {

    override val di by di()
    private val factory:CompanyViewModelFactory by instance()
    private lateinit var viewModel:CompanyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_properties_test)

        var binding=DataBindingUtil.setContentView(this,R.layout.activity_properties_test) as ActivityPropertiesTestBinding
        viewModel=ViewModelProvider(this,factory).get(CompanyViewModel::class.java)
        viewModel.test = intent.getSerializableExtra("TEST") as Test
        binding.viewmodel=viewModel
    }
}
