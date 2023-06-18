package com.example.myfirebaserealtimeproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.myfirebaserealtimeproject.databinding.FragmentFirsBinding
import com.example.myfirebaserealtimeproject.modul.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app


class FirsFragment : Fragment() {
    lateinit var binding: FragmentFirsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var adapter: AdapterRv
    var list=ArrayList<User>()
    lateinit var googleSignInClient:GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_firs, container, false)
        binding= FragmentFirsBinding.bind(view)

        auth= Firebase.auth
        val currentUser = auth.currentUser
        firebaseDatabase= FirebaseDatabase.getInstance()
        reference=firebaseDatabase.getReference("users")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val email=currentUser?.email
        val displayName=currentUser?.displayName
        val phoneNumber=currentUser?.phoneNumber
        val photoUrl=currentUser?.photoUrl
        val uid=currentUser?.uid

        val user= User(email,displayName,phoneNumber,photoUrl.toString(),uid)



        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val filterList= arrayListOf<User>()
                val children=snapshot.children
                for (child in children){
                    val value=child.getValue(User::class.java)
                    if (value != null && uid != value.uid ) {
                        list.add(value)
                    }
                    if (value!=null && value.uid==uid){
                        filterList.add(value)
                    }
                }
                if (filterList.isEmpty()){
                    reference.child(uid!!).setValue(user)
                }

                adapter= AdapterRv(list,object :AdapterRv.OnItemClickListener{
                    override fun onItemClick(user: User) {
                        val bundle=Bundle()
                        bundle.putSerializable("user",user)
                        findNavController().navigate(R.id.action_firsFragment_to_chatWindowFragment,bundle)
                    }

                })
                binding.rv.adapter=adapter
            }


            override fun onCancelled(error: DatabaseError) {

            }

        })
        binding.exit.setOnClickListener {
            googleSignInClient.signOut()
            findNavController().popBackStack()
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }


}