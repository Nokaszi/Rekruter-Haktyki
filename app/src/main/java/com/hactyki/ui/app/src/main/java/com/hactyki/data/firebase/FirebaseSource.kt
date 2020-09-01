package com.hactyki.data.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.hactyki.classes.Test
import com.hactyki.classes.User
import io.reactivex.rxjava3.core.Completable

const val TEST_PATH = "Tests"
const val ANSWERS_PATH = "Answers"
const val USERS_PATH = "Users"

class FirebaseSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firebaseData: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseDatabase.getInstance()
    }
    var testList = MutableLiveData<ArrayList<Test>>(arrayListOf(Test()))
    var userData = MutableLiveData<User>(User())
    var candidateTest = MutableLiveData<Test>()
    var candidatesList = MutableLiveData<ArrayList<User>>(arrayListOf())
    var candidatesKey: ArrayList<String> = arrayListOf()
    var answers = MutableLiveData<ArrayList<String>>(arrayListOf())
    var solvedTestList = MutableLiveData<ArrayList<String>>(arrayListOf())


    fun signIn(email: String, password: String): Completable =
        Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) {
                        emitter.onComplete()
                    } else
                        emitter.onError(task.exception!!)

                }
            }

        }

    fun signUp(email: String, password: String, user: User): Completable =
        Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (!emitter.isDisposed) {
                        if (task.isSuccessful)
                            task.result?.user?.uid?.let { uid ->
                                saveUserToDatabase(user, uid).subscribe({
                                    emitter.onComplete()
                                }, {
                                    emitter.onError(it)
                                })
                            }
                        else
                            emitter.onError(task.exception)
                    }
                }

        }

    fun signOut() = firebaseAuth.signOut()
    fun getUser() = firebaseAuth.currentUser

    fun saveTestToDatabase(test: Test): Completable = Completable.create { emitter ->

        val testReference = firebaseData.getReference(TEST_PATH)
        testReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.child(test.name).exists()) {
                    testReference.child(test.name).setValue(test).addOnCompleteListener {
                        if (!emitter.isDisposed)
                            if (it.isSuccessful) {
                                emitter.onComplete()
                            } else
                                emitter.onError(it.exception)
                    }
                } else {
                    emitter.onError(Throwable("Podana nazwa testu już istnieje"))
                }
            }

        })

    }

    fun readTests(): Completable = Completable.create { emitter ->

        val testReference = firebaseData.getReference(TEST_PATH)
        testReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (tests in snapshot.children) {
                    val test = tests.getValue(Test::class.java)
                    if (test?.author == firebaseAuth.currentUser?.uid)
                        test?.let { testList.value?.add(it) }
                }
                emitter.onComplete()
            }

        })
    }

    private fun saveUserToDatabase(user: User, uid: String): Completable =
        Completable.create { emitter ->
            val userReference = firebaseData.getReference(USERS_PATH)
            userReference.child(uid).setValue(user).addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful)
                        emitter.onComplete()
                    else
                        emitter.onError(task.exception)
                }
            }
        }

    fun readUser(): Completable = Completable.create { emitter ->
        val userReference = firebaseData.getReference(USERS_PATH)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userData.value =
                    getUser()?.uid?.let { snapshot.child(it).getValue(User::class.java) }!!
                emitter.onComplete()
            }
        }
        )
    }

    fun getTest(name: String, password: String): Completable = Completable.create { emitter ->
        val testReference = firebaseData.getReference(TEST_PATH)
        candidateTest.value = Test()
        testReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val temporary = snapshot.child(name).getValue(Test::class.java)
                if (temporary == null) emitter.onError(Throwable("Błędna nazwa lub hasło"))
                else
                    if (temporary.password == password) {
                        candidateTest.value = temporary
                    } else {
                        emitter.onError(Throwable("Błędna nazwa lub hasło"))
                    }
                emitter.onComplete()
            }
        })
    }

    fun saveAnswers(answers: MutableList<String>, nameTest: String): Completable =
        Completable.create { emitter ->
            val answersReference = firebaseData.getReference(ANSWERS_PATH)
            getUser()?.uid?.let { uid ->
                answersReference.child(nameTest).child(uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            emitter.onError(error.toException())
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                emitter.onError(Throwable("Test już został, przez ciebie rozwiązany"))
                            } else {
                                answersReference.child(nameTest).child(uid).setValue(answers)
                                    .addOnCompleteListener {
                                        if (!emitter.isDisposed) {
                                            if (it.isSuccessful) {
                                                emitter.onComplete()
                                            } else {
                                                emitter.onError(it.exception)
                                            }
                                        }
                                    }

                            }
                        }

                    })

            }
        }

    fun getAnswers(testName: String, idCandidate: Int): Completable =
        Completable.create { emitter ->
            answers.value = arrayListOf()
            val candidateUid = candidatesKey[idCandidate]
            val answersReference = firebaseData.getReference(ANSWERS_PATH)
            answersReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (answer in snapshot.child(testName).child(candidateUid).children) {
                        answers.value!!.add(answer.value as String)
                    }
                    emitter.onComplete()
                }

            })

        }

    fun deleteTest(testName: String): Completable = Completable.create { emitter ->
        val testReference = firebaseData.getReference(TEST_PATH)
        testReference.child(testName).removeValue().addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception)
            }
        }
        emitter.onComplete()
    }

    fun getCandidateList(testName: String): Completable = Completable.create { emitter ->
        val candidateReference = firebaseData.getReference(ANSWERS_PATH)
        candidatesList.value = arrayListOf()
        candidatesKey = arrayListOf()
        candidateReference.child(testName).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for (candidate in snapshot.children) {

                    candidate.key?.let { key ->
                        getCandidateInfo(key).subscribe({
                            candidatesKey.add(key)
                            emitter.onComplete()
                        }, {
                            emitter.onError(it)
                        })
                    }


                }
            }

        })
    }

    private fun getCandidateInfo(key: String): Completable = Completable.create { emitter ->
        val userReference = firebaseData.getReference(USERS_PATH)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                candidatesList.value?.add(snapshot.child(key).getValue(User::class.java)!!)
                emitter.onComplete()
            }

        })
    }

    fun getSolvedTests(): Completable = Completable.create { emitter ->
        solvedTestList.value= arrayListOf()
        val testRepository = firebaseData.getReference(ANSWERS_PATH)
        testRepository.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (testAnswer in snapshot.children) {
                    val uid = getUser()?.uid
                    if (uid?.let { testAnswer.child(it).exists() }!!)
                        solvedTestList.value?.add(testAnswer.key.toString())
                }
                emitter.onComplete()
            }

        })

    }

}
