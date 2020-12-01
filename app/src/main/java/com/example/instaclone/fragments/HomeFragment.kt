package com.example.instaclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.instaclone.R

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val send : ImageView = view.findViewById<ImageView>(R.id.sendButton)
        send.setOnClickListener {
            Toast.makeText(activity,"Chat in Maintaenance",Toast.LENGTH_SHORT).show()
        }

        return view
    }

}