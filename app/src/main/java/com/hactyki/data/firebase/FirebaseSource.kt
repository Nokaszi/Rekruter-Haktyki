package com.hactyki.data.firebase

import android.text.BoringLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hactyki.classes.Test
import com.hactyki.ui.auth.SignInActivity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseSource {

    private val firebaseAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    /*private val firebaseData:FirebaseDatabase by lazy{
        FirebaseDatabase.getInstance()
    }*/

     fun signIn(email:String, password:String): Completable =
        Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful)
                        emitter.onComplete()
                    else
                        emitter.onError(task.exception!!)

                }
            }

        }

    fun signUp(email: String,password: String): Completable =
        Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful)
                        emitter.onComplete()
                    else
                        emitter.onError(task.exception!!)

                }
            }

        }
    fun getUser()=firebaseAuth.currentUser

  /*  fun saveTestToDatabase(test:Test){
        var testReferance = firebaseData.getReference("Tests")
        testReferance.child(test.name).setValue(test)
    }*/
}
