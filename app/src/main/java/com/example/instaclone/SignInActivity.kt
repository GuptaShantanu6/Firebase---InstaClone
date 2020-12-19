package com.example.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.widget.Button



class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val sign_up_btn : Button = findViewById<Button>(R.id.sign_up_btn)

        sign_up_btn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        
    }
}