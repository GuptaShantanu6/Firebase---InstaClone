package com.example.instaclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
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

        val anim : LottieAnimationView = view.findViewById<LottieAnimationView>(R.id.homeActivityIcon)
        anim.setAnimation("mainActivityAnim.json")
        anim.playAnimation()
        anim.loop(true)

        return view
    }

}