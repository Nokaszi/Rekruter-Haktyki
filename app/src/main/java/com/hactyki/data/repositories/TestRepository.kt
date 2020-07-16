package com.hactyki.data.repositories

import com.hactyki.classes.Test
import com.hactyki.data.firebase.FirebaseSource

class TestRepository( private val firebase: FirebaseSource) {
    fun saveTestToDatabase(test: Test)=firebase.saveTestToDatabase(test)
}