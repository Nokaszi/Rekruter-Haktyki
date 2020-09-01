package com.hactyki.data.repositories

import com.hactyki.classes.Test
import com.hactyki.data.firebase.FirebaseSource

class TestRepository(private val firebase: FirebaseSource) {
    fun saveTestToDatabase(test: Test) = firebase.saveTestToDatabase(test)
    fun getUser() = firebase.getUser()
    fun readTest() = firebase.readTests()
    fun getTest(name: String, password: String) = firebase.getTest(name, password)
    var testList = firebase.testList
    var candidateTest = firebase.candidateTest
    fun saveAnswers(answers: MutableList<String>, nameTest: String) =
        firebase.saveAnswers(answers, nameTest)

    fun getAnswers(testName: String, idCandidate: Int) = firebase.getAnswers(testName, idCandidate)
    fun getCandidateList(testName: String) = firebase.getCandidateList(testName)
    var candidateList = firebase.candidatesList
    var answers = firebase.answers
    fun signOut() = firebase.signOut()
    fun getSolvedTests() = firebase.getSolvedTests()
    fun deleteTest(testName: String) = firebase.deleteTest(testName)
    var solvedTestsList = firebase.solvedTestList
}