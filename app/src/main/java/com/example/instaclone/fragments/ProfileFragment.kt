package com.example.instaclone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.example.instaclone.AccountSettingsActivity
import com.example.instaclone.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        val usernamesettingsbutton : ImageView = view.findViewById<ImageView>(R.id.usernamesettingsbutton)
        usernamesettingsbutton.setOnClickListener {
            Toast.makeText(activity,"Settings in Maintenance",Toast.LENGTH_SHORT).show()
        }

        val editButton : Button = view.findViewById<Button>(R.id.edit_account_settings_btn)
        editButton.setOnClickListener {
//            Toast.makeText(activity,"Under Maintenance",Toast.LENGTH_SHORT).show()
            startActivity(Intent(context,AccountSettingsActivity::class.java))

        }

        val imagesGrid : ImageButton = view.findViewById<ImageButton>(R.id.images_grid_view_button)
        val imagesSave : ImageButton = view.findViewById<ImageButton>(R.id.images_save_button)

        return view
    }
}