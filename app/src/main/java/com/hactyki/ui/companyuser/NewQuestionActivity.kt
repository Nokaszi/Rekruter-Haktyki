package com.hactyki.ui.companyuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hactyki.R
import com.hactyki.classes.Test
import com.hactyki.databinding.ActivityNewQuestionBinding
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class NewQuestionActivity : AppCompatActivity(),DIAware {

    override val di by di()
    private lateinit var viewModel:CompanyViewModel
    private val factory:CompanyViewModelFactory by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_question)
        val binding:ActivityNewQuestionBinding=DataBindingUtil.setContentView(this,R.layout.activity_new_question)
        viewModel=ViewModelProvider(this,factory).get(CompanyViewModel::class.java)
        viewModel.test=intent.getSerializableExtra("TEST") as Test

        var id=intent.getLongExtra("ID",-1).toInt()
        if (id!=-1){
            viewModel.question=viewModel.test.questions[id]
        }

        binding.viewmodel=viewModel

        binding.openQuestionSwitch.setOnCheckedChangeListener { compoundButton, b ->
                if (b){
                    viewModel.question.isOpen=true
                    binding.answer1Et.isEnabled=false
                    binding.answer2Et.isEnabled=false
                    binding.answer3Et.isEnabled=false
                }
                else{
                    viewModel.question.isOpen=false
                    binding.answer1Et.isEnabled=true
                    binding.answer2Et.isEnabled=true
                    binding.answer3Et.isEnabled=true
                }

        }

    }
}
