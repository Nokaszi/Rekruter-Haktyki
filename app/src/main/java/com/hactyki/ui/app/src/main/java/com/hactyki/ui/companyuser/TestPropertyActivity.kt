package com.hactyki.ui.companyuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hactyki.BUNDLE_KEY_EDITABLE
import com.hactyki.BUNDLE_KEY_TEST
import com.hactyki.R
import com.hactyki.classes.Test
import com.hactyki.databinding.ActivityTestPropertyBinding
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class TestPropertyActivity : AppCompatActivity(), DIAware, CompanyListener {


    override val di by di()
    private val factory: CompanyViewModelFactory by instance()
    private lateinit var viewModel: CompanyViewModel
    lateinit var binding: ActivityTestPropertyBinding
     lateinit var numberQuestion:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_property)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_property)
        viewModel = ViewModelProvider(this, factory).get(CompanyViewModel::class.java)
        viewModel.companyListener = this
        binding.viewmodel = viewModel
        binding.activity = this
        viewModel.test = intent.getSerializableExtra(BUNDLE_KEY_TEST) as Test
        numberQuestion=viewModel.test.questions.size.toString()

    }

    fun goEditTest() {
        Intent(this, NewTestActivity::class.java).also {
            it.putExtra(BUNDLE_KEY_TEST, viewModel.test)
            it.putExtra(BUNDLE_KEY_EDITABLE, true)
            startActivity(it)
        }
    }


    fun showAnswer() {
        Intent(this, CandidateListActivity::class.java).also {
            it.putExtra(BUNDLE_KEY_TEST, viewModel.test)
            startActivity(it)
        }

    }

    fun numberQuestion(): String { return viewModel.test.questions.size.toString()}
    override fun onStarted() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding.progressbar.visibility = View.GONE
        Intent(this, HomeCompanyUserActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onFailed(message: String) {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
