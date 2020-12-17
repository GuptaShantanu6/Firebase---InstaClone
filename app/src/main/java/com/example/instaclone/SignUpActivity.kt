package com.example.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.widget.Button

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val already_act_btn : Button = findViewById<Button>(R.id.already_account_btn)

        already_act_btn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }

    }
}