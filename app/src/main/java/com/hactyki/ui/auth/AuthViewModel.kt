package com.hactyki.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.hactyki.data.repositories.UserRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AuthViewModel(private val repository: UserRepository):ViewModel() {

    var email:String?="tomasz@ja.pl"
    var password:String?="Zaq12wsx"
    var authListener:AuthListener?=null
    private val disposables = CompositeDisposable()


     fun login() {

        if(email.isNullOrEmpty()|| password.isNullOrEmpty())
        {
            authListener?.onFailure("Email and password can be empty")
            return
        }
        authListener?.onStarted()
         val disposable=repository.signIn(email!!,password!!).subscribe({authListener?.onSuccess()},{authListener?.onFailure(it.message.toString())})
         disposables.add(disposable)
    }
    fun register(){
        if(email.isNullOrEmpty()|| password.isNullOrEmpty())
        {
            authListener?.onFailure("Email and password can be empty")
            return
        }
        authListener?.onStarted()
        val disposable=repository.signUp(email!!,password!!).subscribe({authListener?.onSuccess()},{authListener?.onFailure(it.message.toString())})
        disposables.add(disposable)
    }

    fun goToSignUp(view:View){
        Intent(view.context,SignUpActivity::class.java).also{
            view.context.startActivity(it)
        }
    }
    fun goToSignIn(view:View) {
        Intent(view.context, SignInActivity::class.java).also {
            view.context.startActivity(it)
        }
    }
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}