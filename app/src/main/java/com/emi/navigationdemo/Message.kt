package com.emi.navigationdemo


data class Message(
    var id : String = "",
    var userName : String="",
    var userEmail : String="",
    var timestamp: Long= 0,
    var title : String="",
    var message : String="")
