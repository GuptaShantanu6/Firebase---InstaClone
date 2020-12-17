package com.example.instaclone

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.instaclone.fragments.ProfileFragment

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

    }
}