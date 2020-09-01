package com.hactyki.ui.companyuser

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hactyki.BUNDLE_KEY_EDITABLE
import com.hactyki.BUNDLE_KEY_ID
import com.hactyki.BUNDLE_KEY_TEST
import com.hactyki.R
import com.hactyki.classes.Test
import com.hactyki.databinding.ActivityNewTestBinding
import com.hactyki.databinding.ActivityTestPropertiesBinding
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

const val MIN_NUMBER_OF_QUESTION = 2

class NewTestActivity : AppCompatActivity(), DIAware, CompanyListener {

    override val di by di()
    private val factory: CompanyViewModelFactory by instance()
    private lateinit var viewModel: CompanyViewModel
    lateinit var binding: ActivityNewTestBinding
    lateinit var bindingSaveDialog: ActivityTestPropertiesBinding
    private var editableTest: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_test)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_new_test)
        viewModel = ViewModelProvider(this, factory).get(CompanyViewModel::class.java)
        viewModel.test = intent.getSerializableExtra(BUNDLE_KEY_TEST) as Test
        editableTest = intent.getBooleanExtra(BUNDLE_KEY_EDITABLE, false)
        viewModel.companyListener = this
        binding.activity = this

        binding.questionListView.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, viewModel.questionList())

        binding.questionListView.setOnItemClickListener { parent, view, position, id ->
            itemClick(id.toInt())
        }
    }

    private fun itemClick(id: Int) {
        Intent(this, NewQuestionActivity::class.java).also {
            it.putExtra(BUNDLE_KEY_ID, id)
            it.putExtra(BUNDLE_KEY_TEST, viewModel.test)
            it.putExtra(BUNDLE_KEY_EDITABLE, editableTest)
            startActivity(it)
        }
    }

    fun goToTestProperties() {
        if (viewModel.test.questions.size >= MIN_NUMBER_OF_QUESTION) {
            bindingSaveDialog = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.activity_test_properties,
                null,
                false
            )
            bindingSaveDialog.activity = this
            bindingSaveDialog.viewmodel = viewModel
            val dialog =
                AlertDialog.Builder(bindingSaveDialog.root.context).setView(bindingSaveDialog.root)
            dialog.show()

        } else {
            val dialogAlert = AlertDialog.Builder(this).setCancelable(true).setTitle("Błąd")
                .setMessage("Test powinien zawierać co najmniej" + MIN_NUMBER_OF_QUESTION + "pytania")
            dialogAlert.show()
        }
    }

    fun goToNewQuestion() {
        Intent(this, NewQuestionActivity::class.java).also {
            it.putExtra(BUNDLE_KEY_TEST, viewModel.test)
            it.putExtra(BUNDLE_KEY_EDITABLE, editableTest)
            startActivity(it)
        }
    }

    override fun onStarted() {
        bindingSaveDialog.progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        bindingSaveDialog.progressbar.visibility = View.GONE
        val successDialog = AlertDialog.Builder(this).setCancelable(true).setTitle("Test zapisany").setMessage("Test został zapisany poprawnie").setPositiveButton(
            "ok", DialogInterface.OnClickListener(positiveClick)
        )
        successDialog.show()

    }

    private val positiveClick = { dialog: DialogInterface, which: Int ->
        goToHome()
    }

    private fun goToHome() {
        Intent(this, HomeCompanyUserActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun onFailed(message: String) {
        bindingSaveDialog.progressbar.visibility = View.GONE
        bindingSaveDialog.errorMessage.text = message
    }
}
