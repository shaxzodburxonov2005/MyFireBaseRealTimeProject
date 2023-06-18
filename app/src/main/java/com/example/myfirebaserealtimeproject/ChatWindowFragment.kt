package com.example.myfirebaserealtimeproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myfirebaserealtimeproject.databinding.FragmentChatWindowBinding
import com.example.myfirebaserealtimeproject.modul.MessageChat
import com.example.myfirebaserealtimeproject.modul.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatWindowFragment : Fragment() {
    lateinit var binding:FragmentChatWindowBinding

    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var adapter: MessageAdapter
    lateinit var reference: DatabaseReference




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_chat_window, container, false)
        binding= FragmentChatWindowBinding.bind(view)

        auth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        val user = arguments?.getSerializable("user") as User
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        Picasso.get().load(user.photoUrl).into(binding.imageEmail)
        binding.nameEmail.text=user.email
        if (user.online){
            binding.onlayin.text="tarmoqda"
        }else {
            binding.onlayin.text="yaqinda onlayin edi"
        }



        binding.sendButton.setOnClickListener {
            val m = binding.messageEt.text.toString()
            if (m.isNotBlank()) {
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
                val date = simpleDateFormat.format(Date())
                val message = MessageChat(m, date, auth.currentUser?.uid, user.uid)
                val key = reference.push().key

                reference.child("/${auth.currentUser?.uid}/messages/${user.uid!!}/$key")
                    .setValue(message)
                reference.child("${user.uid}/messages/${auth.currentUser?.uid}/$key")
                    .setValue(message)
                binding.messageEt.text.clear()
            }
        }
        reference.child("${auth.currentUser?.uid}/messages/${user.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<MessageChat>()
                    val children = snapshot.children
                    for (child in children) {
                        val value = child.getValue(MessageChat::class.java)
                        if (value != null) {
                            list.add(value)
                        }
                    }

                    adapter = MessageAdapter(list, auth.currentUser!!.uid)
                    binding.rvMessage.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        return view
    }


}