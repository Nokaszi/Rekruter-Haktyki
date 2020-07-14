package com.hactyki.ui.companyuser

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.hactyki.classes.Questions
import com.hactyki.classes.Test
import com.hactyki.data.repositories.TestRepository
import com.hactyki.data.repositories.UserRepository

class CompanyViewModel( private val testRepository: TestRepository): ViewModel(){


     var question:Questions?= Questions("",true,null)
     var test=Test("Jakis test","Haslo",null,"")


    fun goToNewTest(view: View){
        Intent(view.context,NewTestActivity::class.java).also{
            view.context.startActivity(it)
        }
    }
    fun goToNewQuestion(view:View){
        Intent(view.context,NewQuestionActivity::class.java).also {
            view.context.startActivity(it)
        }
    }
    fun pushQuestionToTest(view: View){
        test.questions?.add(question!!)
        question=null
        goToNewTest(view)
    }

    fun saveTest(){
       // testRepository.saveTestToDatabase(test)
    }
}