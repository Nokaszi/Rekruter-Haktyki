package com.hactyki.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hactyki.R
import com.hactyki.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class SignUpActivity : AppCompatActivity(),AuthListener,DIAware {

    override val di by di()

    private val factory:AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        val binding:ActivitySignUpBinding=DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        viewModel=ViewModelProvider(this,factory).get(AuthViewModel::class.java)
        binding.viewmodel=viewModel
        viewModel.authListener=this
    }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressbar.visibility=View.GONE
        Toast.makeText(this,"Sukces", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(messege: String) {
        progressbar.visibility=View.GONE
        Toast.makeText(this,messege,Toast.LENGTH_SHORT).show()
    }
}
