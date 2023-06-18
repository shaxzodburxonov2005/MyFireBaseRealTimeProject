package com.example.myfirebaserealtimeproject.modul

data class MessageChat(
    var message: String? = null,
    var date: String? = null,
    var fromUid: String? = null,
    var toUid: String? = null
)
