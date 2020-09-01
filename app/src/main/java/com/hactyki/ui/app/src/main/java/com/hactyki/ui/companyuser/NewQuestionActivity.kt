package com.hactyki.ui.companyuser

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hactyki.BUNDLE_KEY_EDITABLE
import com.hactyki.BUNDLE_KEY_ID
import com.hactyki.BUNDLE_KEY_TEST
import com.hactyki.R
import com.hactyki.classes.Test
import com.hactyki.databinding.ActivityNewQuestionBinding
import kotlinx.android.synthetic.main.activity_test_candidate.*
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class NewQuestionActivity : AppCompatActivity(), DIAware, CompanyListener {

    override val di by di()
    private lateinit var viewModel: CompanyViewModel
    private val factory: CompanyViewModelFactory by instance()
    lateinit var binding: ActivityNewQuestionBinding
    var editableTest = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_question)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_new_question)
        viewModel = ViewModelProvider(this, factory).get(CompanyViewModel::class.java)
        viewModel.test = intent.getSerializableExtra(BUNDLE_KEY_TEST) as Test
        val questionId = intent.getIntExtra(BUNDLE_KEY_ID, -1)
        editableTest = intent.getBooleanExtra(BUNDLE_KEY_EDITABLE, false)
        if (questionId != -1) {
            viewModel.question = viewModel.test.questions[questionId]
            viewModel.idQuestion = questionId
        }
        binding.viewmodel = viewModel
        binding.activity = this
        viewModel.companyListener=this
    }

    fun questionToTest() {
        viewModel.pushQuestionToTest()
    }

    fun switchClick() {
        if (binding.switchIsOpen.isChecked) {
            binding.answer1Til.visibility = View.GONE
            binding.answer3Til.visibility = View.GONE
            binding.answer2Til.visibility = View.GONE
            binding.answer4Til.visibility = View.GONE
        } else {
            binding.answer1Til.visibility = View.VISIBLE
            binding.answer3Til.visibility = View.VISIBLE
            binding.answer2Til.visibility = View.VISIBLE
            binding.answer4Til.visibility = View.VISIBLE
        }
    }

    fun deleteQuestion(){
        viewModel.test.questions.removeAt(viewModel.idQuestion)
        Intent(this, NewTestActivity::class.java).also {
            it.putExtra(BUNDLE_KEY_TEST, viewModel.test)
            it.putExtra(BUNDLE_KEY_EDITABLE, editableTest)
            startActivity(it)
            finish()
        }

    }
    override fun onStarted() {
    }

    override fun onSuccess() {
        Intent(this, NewTestActivity::class.java).also {
            it.putExtra(BUNDLE_KEY_TEST, viewModel.test)
            it.putExtra(BUNDLE_KEY_EDITABLE, editableTest)
            startActivity(it)
            finish()
        }

    }

    override fun onFailed(message: String) {
        val dialog = AlertDialog.Builder(this).setTitle("Błąd pytania").setMessage(message)
            .setCancelable(true)
        dialog.show()
    }
}
