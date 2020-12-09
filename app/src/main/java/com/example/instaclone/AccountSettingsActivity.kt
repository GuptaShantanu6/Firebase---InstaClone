package com.example.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class AccountSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        val close_btn : ImageView = findViewById<ImageView>(R.id.close_btn_account_settings)
        val logout : Button = findViewById<Button>(R.id.logout_btn_account_settings)


    }
}