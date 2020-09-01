package com.hactyki.ui.candidateuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hactyki.BUNDLE_KEY_TEST
import com.hactyki.R
import com.hactyki.databinding.ActivityHomeCandidateBinding
import com.hactyki.ui.auth.SignInActivity
import kotlinx.android.synthetic.main.solve_test_dialog.*
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class HomeCandidateActivity : AppCompatActivity(), DIAware, CandidateListener {

    override val di by di()
    private val factory: CandidateViewModelFactory by instance()
    private lateinit var viewModel: CandidateViewModel
    lateinit var binding: ActivityHomeCandidateBinding
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_candidate)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home_candidate
        )
        viewModel = ViewModelProvider(this, factory).get(CandidateViewModel::class.java)
        binding.viewmodel = viewModel
        binding.activity = this
        viewModel.candidateListener = this
    }

    fun openDialog() {
        dialog = AlertDialog.Builder(this).setView(R.layout.solve_test_dialog).show()
        dialog.dialogLoginBtn.setOnClickListener {
            viewModel.testPassword = dialog.dialogPasswordEt.text.toString()
            viewModel.testName = dialog.dialogNameEt.text.toString()
            viewModel.loadTest()

        }
        dialog.dialogCancelBtn.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun startTest() {
        Intent(this, TestCandidateActivity::class.java).also {
            it.putExtra(BUNDLE_KEY_TEST, viewModel.candidateTest)
            startActivity(it)
            finish()
        }
    }

    override fun onStarted() {
        dialog.progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        dialog.progressbar.visibility = View.GONE
        startTest()
    }

    override fun onFailed(message: String) {
        dialog.progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun signOut() {
        viewModel.signOut()
        Intent(this, SignInActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
    fun goToSolvedTest(){
        Intent(this,SolvedTestsListActivity::class.java).also {
            startActivity(it)
        }
    }
}
