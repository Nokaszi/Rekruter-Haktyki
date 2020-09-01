package com.hactyki.classes

import java.io.Serializable

data class Test(var name:String="", var password:String="", var questions: MutableList<Questions> = mutableListOf(),  var  author:String=""):Serializable {
}