package com.example.instaclone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class UserClickFromSearchActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_click_from_search)

        val back_button = findViewById<ImageView>(R.id.back_button)
        back_button.setOnClickListener {
            startActivity(Intent(baseContext,MainActivity::class.java))
        }

        val pref = baseContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val otherUserUID = pref.getString("profileID","none").toString()

        val usernameText : TextView = findViewById<TextView>(R.id.usernameText)
        val fullnameText : TextView = findViewById<TextView>(R.id.full_name_profile_frag)
        val bioText : TextView = findViewById<TextView>(R.id.bio_profile_frag)
        val totalPosts : TextView = findViewById<TextView>(R.id.total_posts)
        val followersText : TextView = findViewById<TextView>(R.id.total_followers)
        val followingText : TextView = findViewById<TextView>(R.id.total_following)

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("Follow").exists()){
                    if (snapshot.child("Follow").child(otherUserUID).exists()){
                        if (snapshot.child("Follow").child(otherUserUID).child("Followers").exists()){
                            followersText.text = snapshot.child("Follow").child(otherUserUID).child("Followers").childrenCount.toString()
                        }
                        if (snapshot.child("Follow").child(otherUserUID).child("Following").exists()){
                            followingText.text = snapshot.child("Follow").child(otherUserUID).child("Following").childrenCount.toString()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"An error has occurred",Toast.LENGTH_SHORT).show()
            }

        })

        database.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                usernameText.text = snapshot.child(otherUserUID).child("username").value.toString()
                var temp = snapshot.child(otherUserUID).child("fullName").value.toString()
                temp = temp.capitalizeFirstLetter()
                fullnameText.text = temp
                bioText.text = snapshot.child(otherUserUID).child("bio").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        val proImage : CircleImageView = findViewById<CircleImageView>(R.id.pro_image_profile_frag)

        val photoStorage = FirebaseStorage.getInstance().reference
        photoStorage.child("Default Images").child(otherUserUID).downloadUrl.addOnSuccessListener {
            val x = it.toString()
            Glide.with(this)
                    .load(x)
                    .into(proImage)
        }.addOnFailureListener {
//            Toast.makeText(this,"Unable to load Photo",Toast.LENGTH_SHORT).show()
            //the default profile image is shown.
        }
//        Log.d("other User Id",otherUserUID)

    }

    fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }.trimEnd()
}