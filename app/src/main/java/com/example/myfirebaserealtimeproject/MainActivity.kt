package com.example.myfirebaserealtimeproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfirebaserealtimeproject.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    var currentUser: FirebaseUser? = null

    override fun onPause() {
        super.onPause()
        currentUser = auth.currentUser

        if (currentUser != null) {
            reference.child(currentUser?.uid!!).child("online").setValue(false)
        }
    }

    override fun onStart() {
        super.onStart()
        currentUser = auth.currentUser

        if (currentUser != null) {
            reference.child("${currentUser?.uid!!}/online").setValue(true)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

    }
}