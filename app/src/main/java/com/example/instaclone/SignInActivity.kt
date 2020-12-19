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

        val new_Act_btn : Button = findViewById<Button>(R.id.new_act_btn)

        new_Act_btn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }


        
    }
}