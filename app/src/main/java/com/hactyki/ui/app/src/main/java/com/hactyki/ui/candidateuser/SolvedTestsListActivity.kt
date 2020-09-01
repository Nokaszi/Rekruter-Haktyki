package com.hactyki.ui.candidateuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hactyki.R
import com.hactyki.databinding.ActivitySolvedTestsListBinding
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class SolvedTestsListActivity : AppCompatActivity(), DIAware, CandidateListener {

    override val di by di()
    private val factory: CandidateViewModelFactory by instance()
    private lateinit var viewModel: CandidateViewModel
    lateinit var binding:ActivitySolvedTestsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solved_tests_list)
        viewModel=ViewModelProvider(this,factory).get(CandidateViewModel::class.java)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_solved_tests_list)
        binding.viewmodel=viewModel
        binding.activity=this
        viewModel.candidateListener=this
        viewModel.getTestSolved()
        viewModel.solvedTestsList.observe(this, Observer {testsList ->
            binding.testsListView.adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1, testsList)
        })
    }

    override fun onStarted() {
        binding.progressbar.visibility= View.VISIBLE
    }

    override fun onSuccess() {
        binding.progressbar.visibility= View.GONE
    }

    override fun onFailed(message: String) {
        binding.progressbar.visibility= View.GONE
        val dialogAlert =
            AlertDialog.Builder(this).setTitle("Błąd").setMessage(message).setCancelable(true)
        dialogAlert.show()
    }
}
