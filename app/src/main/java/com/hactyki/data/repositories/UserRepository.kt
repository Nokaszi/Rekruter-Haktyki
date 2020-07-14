package com.hactyki.data.repositories

import com.hactyki.data.firebase.FirebaseSource

class UserRepository ( private val firebase:FirebaseSource)
{
    fun signIn(email:String, password:String)=firebase.signIn(email,password)
    fun signUp(email: String , password: String) =firebase.signUp(email,password)
    fun getUser()=firebase.getUser()

}