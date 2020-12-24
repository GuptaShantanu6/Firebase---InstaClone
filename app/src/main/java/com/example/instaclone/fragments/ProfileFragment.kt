package com.example.instaclone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.instaclone.AccountSettingsActivity
import com.example.instaclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

        var firebaseUser = FirebaseDatabase.getInstance().reference.child("Users")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        val usernameUpdated : TextView = view.findViewById<TextView>(R.id.usernameText)
        val fullnameUpdated : TextView = view.findViewById<TextView>(R.id.full_name_profile_frag)
        val bioUpdated : TextView = view.findViewById<TextView>(R.id.bio_profile_frag)

        firebaseUser.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
//                usernameUpdated.text = snapshot.child(currentUserId).child("username").value as CharSequence?
//                fullnameUpdated.text = snapshot.child(currentUserId).child("fullName").value as CharSequence?
//                var s : CharSequence? = snapshot.child(currentUserId).child("fullName").value as CharSequence?

                val u1 = snapshot.child(currentUserId).child("fullName").value as CharSequence?
                val u2 : String = u1.toString().capitalizeFirstLetter()

                usernameUpdated.text = snapshot.child(currentUserId).child("username").value as CharSequence?
                fullnameUpdated.text = u2
                bioUpdated.text = snapshot.child(currentUserId).child("bio").value as CharSequence?

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        return view
    }

    fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize() }.trimEnd()
}

