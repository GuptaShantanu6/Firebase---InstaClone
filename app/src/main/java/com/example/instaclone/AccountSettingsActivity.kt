package com.example.instaclone

import android.app.ProgressDialog
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.instaclone.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AccountSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        val close_btn : ImageView = findViewById<ImageView>(R.id.close_btn_account_settings)
        val logout : Button = findViewById<Button>(R.id.logout_btn_account_settings)

        val save_btn : ImageView = findViewById<ImageView>(R.id.save_btn_accout_Settings)

        close_btn.setOnClickListener {
            startActivity(Intent(baseContext,MainActivity::class.java))
        }

        save_btn.setOnClickListener {
            Toast.makeText(this,"Save in Maintenance",Toast.LENGTH_SHORT).show()
        }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this,"Successfully Logged Out",Toast.LENGTH_SHORT).show()
            val intent = Intent(this@AccountSettingsActivity,SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        val deleteAct_btn : Button = findViewById<Button>(R.id.delete_account_btn_account_settings)
        deleteAct_btn.setOnClickListener {

        }

    }
}