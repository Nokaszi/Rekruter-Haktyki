package com.hactyki.ui.candidateuser

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hactyki.classes.Questions
import com.hactyki.classes.Test
import com.hactyki.data.repositories.TestRepository
import io.reactivex.rxjava3.core.Completable

class CandidateViewModel(private val testRepository: TestRepository) : ViewModel() {

    var testName: String? = ""
    var testPassword: String? = ""
    var candidateTest = Test()
    var answers: MutableList<String> = mutableListOf()
    var answersIds: MutableList<Int> = mutableListOf()
    var candidateListener: CandidateListener? = null
    var question = MutableLiveData<Questions>(Questions())
    var solvedTestsList = MutableLiveData<ArrayList<String>>(arrayListOf())
    var numberquestion = 0

    fun initializationAnswers() {
        question.value = candidateTest.questions[0]
        for (i in 0 until candidateTest.questions.size) {
            answers.add("")
            answersIds.add(-1)
        }
    }

    fun loadTest() {
        candidateListener?.onStarted()
        if (testName.isNullOrEmpty() || testPassword.isNullOrEmpty()) {
            candidateListener?.onFailed("Wprowadź nazwę testu i hasło")
        }
        testName?.let { name ->
            testPassword?.let { password ->
                testRepository.getTest(name, password).subscribe({
                    candidateTest = testRepository.candidateTest.value!!
                    candidateListener?.onSuccess()
                }, {
                    it.message?.let { message -> candidateListener?.onFailed(message) }
                })
            }
        }
    }


    fun getNextQuestion(openAnswer: String, closedAnswer: String, closedAnswerId: Int) {
        if (numberquestion < candidateTest.questions.size) {
            if (candidateTest.questions[numberquestion].isOpen) {
                answers[numberquestion] = openAnswer
            } else {
                answers[numberquestion] = closedAnswer
                answersIds[numberquestion] = closedAnswerId
            }
            if (numberquestion < candidateTest.questions.size - 1) {
                numberquestion++
                question.value = candidateTest.questions[numberquestion]
            }
        } else {
            candidateListener?.onFailed("Nie można wczytać pytania")
            question.value = candidateTest.questions[numberquestion]
        }
    }

    fun previousQuestion(openAnswer: String, closedAnswer: String, closedAnswerId: Int) {
        if (numberquestion > 0) {
            if (candidateTest.questions[numberquestion].isOpen) {
                answers[numberquestion] = openAnswer
            } else {
                answers[numberquestion] = closedAnswer
                answersIds[numberquestion] = closedAnswerId
            }
            numberquestion--
            question.value = candidateTest.questions[numberquestion]
        } else {
            candidateListener?.onFailed("Nie można wczytać pytania")
            question.value = candidateTest.questions[numberquestion]
        }
    }

    fun saveAnswer(openAnswer: String, closedAnswer: String) {
        if (candidateTest.questions[numberquestion].isOpen) {
            answers[numberquestion] = openAnswer
        } else {
            answers[numberquestion] = closedAnswer
        }
        candidateListener?.onStarted()
        testRepository.saveAnswers(answers, candidateTest.name)
            .subscribe({ candidateListener?.onSuccess() }, {
                it.message?.let { message ->
                    candidateListener?.onFailed(message)
                }
            })
    }

    fun signOut() {
        testRepository.signOut()
    }

    fun getTestSolved() {
        candidateListener?.onStarted()
        testRepository.getSolvedTests().subscribe({
            solvedTestsList.value = testRepository.solvedTestsList.value
            candidateListener?.onSuccess()
        }, {
            it.message?.let { message -> candidateListener?.onFailed(message) }
        }
        )
    }
}