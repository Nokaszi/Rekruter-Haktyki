package com.hactyki.ui.companyuser


import androidx.lifecycle.*
import com.hactyki.classes.Questions
import com.hactyki.classes.Test
import com.hactyki.classes.User
import com.hactyki.data.repositories.TestRepository
import io.reactivex.rxjava3.core.Completable

class CompanyViewModel(private val testRepository: TestRepository) : ViewModel() {


    var companyListener: CompanyListener? = null
    var question: Questions = Questions()
    var test = Test()
    var idQuestion = -1
    var testList = MutableLiveData<List<Test>>(arrayListOf(test))
    var candidatesList = MutableLiveData<ArrayList<User>>(arrayListOf())
    var answers = MutableLiveData<ArrayList<String>>(arrayListOf())

    fun pushQuestionToTest() {
        if (question == Questions() || question.contents == "") {
            companyListener?.onFailed("Pytanie jest puste")
            return
        }

        if (!question.isOpen && (question.answer == Questions().answer)) {
            companyListener?.onFailed("Pytanie zamknięte musi zawierać odpowiedzi")
            return
        }
        if (question.isOpen) question.answer = arrayListOf("", "", "", "")
        if (idQuestion == -1) {
            test.questions.add(question)
            companyListener?.onSuccess()
        } else {
            test.questions[idQuestion] = question
            companyListener?.onSuccess()
        }
    }

    fun questionList(): ArrayList<String> {
        val result: ArrayList<String> = arrayListOf()
        for (q in test.questions) {
            result.add(q.contents)
        }
        return result
    }

    fun saveTest() {
        if (test == Test() || test.name.isEmpty()) {
            companyListener?.onFailed("Nazwa i test nie mogą być puste")
            return
        }
        test.author = testRepository.getUser()?.uid.toString()
        testRepository.saveTestToDatabase(test).subscribe({ companyListener?.onSuccess() }, {
            it.message?.let { message ->
                companyListener?.onFailed(
                    message
                )
            }
        })
    }

    fun deleteTest() {
        companyListener?.onStarted()
        testRepository.deleteTest(test.name).subscribe(
            { companyListener?.onSuccess() },
            { it.message?.let { message -> companyListener?.onFailed(message) } }
        )
    }

    fun getCandidateList(): LiveData<ArrayList<User>> {
        companyListener?.onStarted()
        testRepository.getCandidateList(test.name).subscribe(
            {
                candidatesList.value = testRepository.candidateList.value
                companyListener?.onSuccess()
            },
            { it.message?.let { message -> companyListener?.onFailed(message) } }
        )
        return candidatesList
    }

    fun readTestsList(): LiveData<List<Test>> {
        companyListener?.onStarted()
        testRepository.readTest().subscribe({
            testList.value = testRepository.testList.value
            companyListener?.onSuccess()
        }, { companyListener?.onFailed(it.message.toString()) })
        testRepository.testList.value = arrayListOf()
        return testList
    }

    fun getAnswers(idCandidate: Int): Completable = Completable.create { emitter ->
        testRepository.getAnswers(test.name, idCandidate).subscribe({
            answers.value = testRepository.answers.value
            emitter.onComplete()
        }, {
            emitter.onError(it)
        })
    }
    fun signOut(){
        testRepository.signOut()
    }
}
