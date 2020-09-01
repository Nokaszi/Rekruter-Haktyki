package com.hactyki.ui.companyuser


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hactyki.BUNDLE_KEY_TEST
import com.hactyki.R
import com.hactyki.classes.Test
import com.hactyki.classes.User
import com.hactyki.databinding.ActivityCandidateListBinding
import kotlinx.android.synthetic.main.answers_dialog.*
import kotlinx.android.synthetic.main.solve_test_dialog.*
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class CandidateListActivity : AppCompatActivity(), DIAware, CompanyListener {

    override val di by di()
    private val factory: CompanyViewModelFactory by instance()
    private lateinit var viewModel: CompanyViewModel
    lateinit var binding: ActivityCandidateListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_candidate_list)
        viewModel = ViewModelProvider(this, factory).get(CompanyViewModel::class.java)
        viewModel.test = intent.getSerializableExtra(BUNDLE_KEY_TEST) as Test
        binding.viewmodel = viewModel
        viewModel.companyListener = this
        binding.activity = this
        viewModel.getCandidateList().observe(this, Observer {
            updateListView(it)
        })
        binding.swiperefresh.setOnRefreshListener {
            viewModel.getCandidateList()
        }
        binding.candidateListView.setOnItemClickListener { parent, view, position, id ->
            viewModel.getAnswers(id.toInt()).subscribe({
                viewModel.answers.observe(this, Observer {
                    val arrayListAnswers = arrayListOf<String>()
                    for (i in 0 until viewModel.test.questions.size) {
                        arrayListAnswers.add(viewModel.test.questions[i].contents + "\n"+ it[i]+"\n \n")
                    }
                    openAnswers(arrayListAnswers)
                })
            }, {
                it.message?.let { message -> onFailed(message) }
            })

        }
    }

    override fun onStarted() {
        binding.swiperefresh.isRefreshing = true
    }

    override fun onSuccess() {
        binding.swiperefresh.isRefreshing = false
        Toast.makeText(this, "Lista jest aktualna", Toast.LENGTH_LONG).show()
    }

    override fun onFailed(message: String) {
        binding.swiperefresh.isRefreshing = false
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun updateListView(list: ArrayList<User>) {
        val candidatesList = arrayListOf<String>()
        for (user in list) {
            candidatesList.add(user.name + " " + user.surname)
        }
        binding.candidateListView.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, candidatesList)

    }

    private fun openAnswers(answers: ArrayList<String>) {
        val dialog =
            AlertDialog.Builder(this).setView(R.layout.answers_dialog).setCancelable(true).show()
        dialog.answer_list.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, answers)
    }
}
