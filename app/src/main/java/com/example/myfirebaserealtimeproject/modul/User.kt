package com.example.myfirebaserealtimeproject.modul

data class User(
    var email: String? = null,
    var displayName: String? = null,
    var phoneNumber: String? = null,
    var photoUrl: String? = null,
    var uid: String? = null,
    var online: Boolean = true
) : java.io.Serializable
