package com.hactyki.ui.companyuser

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hactyki.BUNDLE_KEY_EDITABLE
import com.hactyki.BUNDLE_KEY_TEST
import com.hactyki.R
import com.hactyki.classes.Test
import com.hactyki.data.firebase.FirebaseSource
import com.hactyki.databinding.ActivityHomeCompanyUserBinding
import com.hactyki.ui.auth.SignInActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class HomeCompanyUserActivity : AppCompatActivity(), DIAware, CompanyListener {

    override val di by di()
    private val factory: CompanyViewModelFactory by instance()
    lateinit var binding: ActivityHomeCompanyUserBinding
    private lateinit var viewModel: CompanyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_company_user)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_home_company_user)
        viewModel = ViewModelProvider(this, factory).get(CompanyViewModel::class.java)
        binding.viewmodel = viewModel
        binding.activity = this
        viewModel.companyListener = this

        viewModel.readTestsList().observe(this, Observer { updateList(it) })
        binding.testListView.setOnItemClickListener { parent, view, position, id ->
            itemClick(id.toInt())
        }
    }

    private fun itemClick(id: Int) {
        viewModel.test = viewModel.testList.value?.get(id)!!
        Intent(this, TestPropertyActivity::class.java).also {
            it.putExtra(BUNDLE_KEY_TEST, viewModel.test)
            startActivity(it)
        }
    }

    private fun updateList(list: List<Test>) {
        val listNameTest = ArrayList<String>()
        list.forEach {
            listNameTest.add(it.name)
        }
        binding.testListView.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, listNameTest)
    }

    fun goToNewTest() {
        Intent(this, NewQuestionActivity::class.java).also {
            it.putExtra(BUNDLE_KEY_TEST,Test())
            it.putExtra(BUNDLE_KEY_EDITABLE, false)
            startActivity(it)
        }
    }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressbar.visibility = View.GONE
    }

    override fun onFailed(message: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun signOut(){
        viewModel.signOut()
        Intent(this,SignInActivity::class.java).also {
            startActivity(it)
        }
    }
}

