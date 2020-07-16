package com.hactyki.classes

import java.io.Serializable

class Test(var name:String="", var passwoerd:String="", var questions: MutableList<Questions> = emptyList<Questions>().toMutableList(), var  author:String=""):Serializable {
}