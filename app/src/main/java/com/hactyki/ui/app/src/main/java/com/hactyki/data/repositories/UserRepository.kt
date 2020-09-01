package com.hactyki.data.repositories

import com.hactyki.classes.User
import com.hactyki.data.firebase.FirebaseSource

class UserRepository(private val firebase: FirebaseSource) {
    fun signIn(email: String, password: String) = firebase.signIn(email, password)
    fun signUp(email: String, password: String, user: User) = firebase.signUp(email, password, user)
    fun getUser() = firebase.getUser()
    fun readUser() = firebase.readUser()
    var userData = firebase.userData
}