package com.example.myfirebaserealtimeproject

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myfirebaserealtimeproject.databinding.FragmentMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainFragment : Fragment() {
    lateinit var binding:FragmentMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    var RC_SIGN_IN = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        MySharedPreference.init(requireContext())
        MySharedPreference.editSplash==true
        val view= inflater.inflate(R.layout.fragment_main, container, false)
        binding= FragmentMainBinding.bind(view)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        auth = FirebaseAuth.getInstance()




        val animTop= AnimationUtils.loadAnimation(view.context, R.anim.from_top)
        val animBottom= AnimationUtils.loadAnimation(view.context, R.anim.from_bottom)

        binding.helloTv.animation=animTop
        binding.btn.animation=animBottom

        binding.btn.setOnClickListener {
            signIn()


        }



        return view

    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
//                        updateUI(user)
                    findNavController().navigate(R.id.action_mainFragment_to_firsFragment)
                    Toast.makeText(requireActivity(), user?.email, Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
//                        updateUI(null)
                    Toast.makeText(requireActivity(), task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }


}