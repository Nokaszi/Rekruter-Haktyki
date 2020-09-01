package com.hactyki.ui.candidateuser

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hactyki.BUNDLE_KEY_TEST
import com.hactyki.R
import com.hactyki.classes.Questions
import com.hactyki.classes.Test
import com.hactyki.databinding.ActivityTestCandidateBinding
import com.hactyki.ui.auth.SignInActivity
import kotlinx.android.synthetic.main.activity_test_candidate.*
import kotlinx.android.synthetic.main.solve_test_dialog.*
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class TestCandidateActivity : AppCompatActivity(), DIAware, CandidateListener {


    override val di by di()
    private val factory: CandidateViewModelFactory by instance()
    private lateinit var viewModel: CandidateViewModel
    lateinit var binding: ActivityTestCandidateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_candidate)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_test_candidate
        )
        viewModel = ViewModelProvider(this, factory).get(CandidateViewModel::class.java)
        binding.viewmodel = viewModel
        binding.activity = this
        viewModel.candidateListener = this
        viewModel.candidateTest = intent.getSerializableExtra(BUNDLE_KEY_TEST) as Test
        viewModel.initializationAnswers()
        viewModel.question.observe(this, Observer { setQuestion(it) })
    }

    fun previousQuestion() {
        if (binding.closeAnswerRg.checkedRadioButtonId != -1)
            viewModel.previousQuestion(
                binding.openAnswerEt.text.toString(),
                findViewById<RadioButton>(binding.closeAnswerRg.checkedRadioButtonId).text.toString(),
                binding.closeAnswerRg.checkedRadioButtonId
            )
        else
            viewModel.previousQuestion(
                binding.openAnswerEt.text.toString(),
                "",
                binding.closeAnswerRg.checkedRadioButtonId
            )

        if (viewModel.numberquestion == viewModel.candidateTest.questions.size - 1) {
            binding.nextBtn.text = "Zapisz test"
        } else binding.nextBtn.text = "Next"

        if (viewModel.answers[viewModel.numberquestion].isEmpty()) {
            if (viewModel.question.value!!.isOpen)
                binding.openAnswerEt.text.clear()
            else
                binding.closeAnswerRg.check(-1)
        } else {
            if (viewModel.question.value!!.isOpen)
                binding.openAnswerEt.setText(viewModel.answers[viewModel.numberquestion])
            else
                binding.closeAnswerRg.check(viewModel.answersIds[viewModel.numberquestion])
        }
    }

    fun nextQuestion() {
        if (viewModel.numberquestion == viewModel.candidateTest.questions.size - 1) {
            if (binding.closeAnswerRg.checkedRadioButtonId != -1)
                saveAnswer(
                    binding.openAnswerEt.text.toString(),
                    findViewById<RadioButton>(binding.closeAnswerRg.checkedRadioButtonId).text.toString()
                )
            else
                saveAnswer(binding.openAnswerEt.text.toString(), "")
        }
        if (binding.closeAnswerRg.checkedRadioButtonId != -1)
            viewModel.getNextQuestion(
                binding.openAnswerEt.text.toString(),
                findViewById<RadioButton>(binding.closeAnswerRg.checkedRadioButtonId).text.toString(),
                binding.closeAnswerRg.checkedRadioButtonId
            )
        else
            viewModel.getNextQuestion(
                binding.openAnswerEt.text.toString(),
                "",
                binding.closeAnswerRg.checkedRadioButtonId
            )



        if (viewModel.numberquestion == viewModel.candidateTest.questions.size - 1) {
            binding.nextBtn.text = "Zapisz test"
        } else binding.nextBtn.text = "Next"


        if (viewModel.answers[viewModel.numberquestion].isEmpty()) {
            if (viewModel.question.value!!.isOpen)
                binding.openAnswerEt.text.clear()
            else
                binding.closeAnswerRg.check(-1)
        } else {
            if (viewModel.question.value!!.isOpen)
                binding.openAnswerEt.setText(viewModel.answers[viewModel.numberquestion])
            else
                binding.closeAnswerRg.check(viewModel.answersIds[viewModel.numberquestion])
        }
    }

    private fun setQuestion(question: Questions) {
        binding.questionContents.text = question.contents
        if (question.isOpen) {
            binding.openAnswerEt.visibility = View.VISIBLE
            binding.closeAnswerRg.visibility = View.GONE
        } else {
            binding.openAnswerEt.visibility = View.GONE
            binding.closeAnswerRg.visibility = View.VISIBLE
            if (question.answer[0] != "") {
                binding.answer1Rbtn.text = question.answer[0]
                binding.answer1Rbtn.visibility = View.VISIBLE
            } else {
                binding.answer1Rbtn.visibility = View.GONE
            }
            if (question.answer[1] != "") {
                binding.answer2Rbtn.text = question.answer[1]
                binding.answer2Rbtn.visibility = View.VISIBLE
            } else {
                binding.answer2Rbtn.visibility = View.GONE
            }
            if (question.answer[2] != "") {
                binding.answer3Rbtn.text = question.answer[2]
                binding.answer3Rbtn.visibility = View.VISIBLE
            } else {
                binding.answer3Rbtn.visibility = View.GONE
            }
            if (question.answer[3] != "") {
                binding.answer4Rbtn.text = question.answer[3]
                binding.answer4Rbtn.visibility = View.VISIBLE
            } else {
                binding.answer4Rbtn.visibility = View.GONE
            }
        }
        if (viewModel.numberquestion == 0) {
            binding.previousBtn.visibility = View.GONE
        } else {
            binding.previousBtn.visibility = View.VISIBLE
        }
    }

    private fun saveAnswer(openAnswer: String, closedAnswer: String) {
        val dialogSave=AlertDialog.Builder(this).setMessage("Czy na pewno chcesz zapisać odpowiedzi")
        dialogSave.setPositiveButton("Tak"){ dialog, which ->
            viewModel.saveAnswer(openAnswer,closedAnswer)
        }
        dialogSave.setNegativeButton("Nie"){dialog, which ->
            dialog.dismiss()
        }
        dialogSave.show()
    }

    override fun onBackPressed() {
       // super.onBackPressed()
        val dialogexit=AlertDialog.Builder(this).setTitle("Czy napewno chcesz wyjść?").setMessage("Odpowiedzi zostaną zapisane i nie będzie możliwośći ich zmiany")
        dialogexit.setNegativeButton("Nie"){dialog, which ->
            dialog.dismiss()

        }
        dialogexit.setPositiveButton("Tak"){dialog, which->
            finish()

        }
        dialogexit.create()
        dialogexit.show()
    }
    override fun onStarted() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding.progressbar.visibility = View.GONE
        Intent(this, HomeCandidateActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun onFailed(message: String) {
        binding.progressbar.visibility = View.GONE
        val dialog =
            AlertDialog.Builder(this).setTitle("Nie można zapisać odpowiedzi").setMessage(message)
                .setCancelable(true).create()
        dialog.show()

    }

}
