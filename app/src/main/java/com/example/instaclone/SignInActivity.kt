package com.example.instaclone

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val logo : LottieAnimationView = findViewById<LottieAnimationView>(R.id.logo)
        logo.setAnimation("mainActivityAnim.json")
        logo.playAnimation()
        logo.loop(true)

        val new_Act_btn : Button = findViewById<Button>(R.id.new_act_btn)

        new_Act_btn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        val logIn_btn : Button = findViewById<Button>(R.id.log_in_btn)
        logIn_btn.setOnClickListener {
            hideKeyboard()
            logInUser()
        }
    }

    private fun logInUser() {
        val emailEditText : EditText = findViewById<EditText>(R.id.email_logIn)
        val email : String = emailEditText.text.toString()
        val passwordEditText : EditText = findViewById<EditText>(R.id.password_logIn)
        val password : String = passwordEditText.text.toString()

        when{
            TextUtils.isEmpty(email) -> emailEditText.setError("Enter the Email Id")
            TextUtils.isEmpty(password) -> passwordEditText.setError("Enter the password")

            else -> {
                val progressDialog : ProgressDialog = ProgressDialog(this@SignInActivity)
                progressDialog.setTitle("Sign In")
                progressDialog.setTitle("Hang on a Moment")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        progressDialog.dismiss()
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Wrong credentials entered",Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}