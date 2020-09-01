package com.hactyki.ui.auth

import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.lifecycle.*
import com.hactyki.classes.User
import com.hactyki.data.repositories.UserRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable


class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null
    var user: User = User()
    private val disposables = CompositeDisposable()

    fun setUserroleKandydat() {
        user.type = "candidate"
    }

    fun setUserroleRekruter() {
        user.type = "recruiter"
    }

    fun login() {
        if (!checkEmailAndPassword()) return
        authListener?.onStarted()
        val disposable = email?.let { mail ->
            password?.let { password ->
                repository.signIn(mail, password).subscribe({
                    repository.readUser().subscribe({
                        user = repository.userData.value!!
                        authListener?.onSuccess()
                    }, { authListener?.onFailure(it.message.toString()) })
                }, {
                    authListener?.onFailure(it.message.toString())
                })
            }
        }
        disposables.add(disposable)
    }

    fun register() {
        if (!checkEmailAndPassword()) return
        if (user.type == "" || user.name == "" || user.surname == "") {
            authListener?.onFailure("Uzupełnij wszystkie dane")
            return
        }
        authListener?.onStarted()
        val disposable = repository.signUp(email!!, password!!, user)
            .subscribe({ authListener?.onSuccess() },
                { authListener?.onFailure(it.message.toString()) })
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    private fun checkEmailAndPassword(): Boolean {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Uzupełnij wszystkie pola")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            authListener?.onFailure("Błędny email")
            return false
        }
        if (password!!.length < 6) {
            authListener?.onFailure("Zbyt krótkie hasło")
            return false
        }
        return true
    }
}