package com.hactyki.ui.companyuser

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.hactyki.classes.Questions
import com.hactyki.classes.Test
import com.hactyki.data.repositories.TestRepository

class CompanyViewModel( private val testRepository: TestRepository): ViewModel(){


     var question:Questions= Questions()
     var test:Test=Test()


    fun goToNewTest(view: View){
        Intent(view.context,NewTestActivity::class.java).also{
            it.putExtra("TEST", test)
            view.context.startActivity(it)
        }
    }
    fun goToNewQuestion(view: View){

        Intent(view.context,NewQuestionActivity::class.java).also {
            it.putExtra("TEST", test)
            view.context.startActivity(it)
        }
    }
    fun pushQuestionToTest(view: View) {
        test.questions.add(question)
        Intent(view.context, NewTestActivity::class.java).also {
            it.putExtra("TEST", test)
            view.context.startActivity(it)
        }
    }
    fun goToPropertiesTest(view: View){
        Intent(view.context, PropertiesTestActivity::class.java).also {
            it.putExtra("TEST",test)
            view.context.startActivity(it)
        }

    }
    fun saveTest(view: View){
        test.let { testRepository.saveTestToDatabase(it) }

    }
}