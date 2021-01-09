package com.example.instaclone

import android.app.ProgressDialog
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.instaclone.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class AccountSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        val currentUser = FirebaseAuth.getInstance().currentUser!!

        val close_btn : ImageView = findViewById<ImageView>(R.id.close_btn_account_settings)
        val logout : Button = findViewById<Button>(R.id.logout_btn_account_settings)

        val save_btn : ImageView = findViewById<ImageView>(R.id.save_btn_accout_Settings)

        val newFullName : EditText = findViewById<EditText>(R.id.change_name_account_settings)
        val newUserName : EditText = findViewById<EditText>(R.id.username_account_settings)
        val newBio : EditText = findViewById<EditText>(R.id.bio_account_settings)

        close_btn.setOnClickListener {
            startActivity(Intent(baseContext,MainActivity::class.java))
        }

        save_btn.setOnClickListener {
//            Toast.makeText(this,"Save in Maintenance",Toast.LENGTH_SHORT).show()
            val database = FirebaseDatabase.getInstance().reference.child("Users").child(currentUser.uid)
            val newMap = HashMap<String,Any>()

            val f = newFullName.text.toString()
            val u = newUserName.text.toString()
            val b = newBio.text.toString()

            if (f.isNotBlank())newMap["fullName"] = f.toLowerCase(Locale.ROOT)
            if (u.isNotBlank())newMap["username"] = u.toLowerCase(Locale.ROOT)
            if (b.isNotBlank())newMap["bio"] = b

            database.updateChildren(newMap)
            Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
            backToMain()

        }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this,"Successfully Logged Out",Toast.LENGTH_SHORT).show()
            backToMain()

        }

        val deleteAct_btn : Button = findViewById<Button>(R.id.delete_account_btn_account_settings)
        deleteAct_btn.setOnClickListener {

        }

    }

    private fun backToMain() {
        val intent = Intent(this@AccountSettingsActivity,SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}