package com.example.instaclone

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.transition.Explode
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val already_act_btn : Button = findViewById<Button>(R.id.already_account_btn)

        already_act_btn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }

        val sign_up_btn : Button = findViewById<Button>(R.id.signup_btn)

        sign_up_btn.setOnClickListener {
            CreateAccount()
        }

    }

    private fun CreateAccount() {
        val fullname_edit_txt : EditText = findViewById<EditText>(R.id.fullName_edit_text)
        val full_name : String = fullname_edit_txt.text.toString()
        val username_Edit_txt : EditText = findViewById<EditText>(R.id.username_edit_txt)
        val user_name : String = username_Edit_txt.text.toString()
        val emailId_edit_txt : EditText = findViewById<EditText>(R.id.email_edit_text)
        val email_id : String = emailId_edit_txt.text.toString()
        val password_edit_txt : EditText = findViewById<EditText>(R.id.password_edit_text)
        val password : String = password_edit_txt.text.toString()

        when{
            TextUtils.isEmpty(full_name) -> fullname_edit_txt.error = "Name cannot be Empty"
            TextUtils.isEmpty(user_name) -> username_Edit_txt.error = "Username cannot be Empty"
            TextUtils.isEmpty(email_id) -> emailId_edit_txt.error = "Email Id cannot be Empty"
            TextUtils.isEmpty(password) -> password_edit_txt.error = "Password cannot be Empty"

            else -> {
                val progressDialog = ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Please Wait, this may take a while")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.createUserWithEmailAndPassword(email_id,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            saveUserInfo(full_name,user_name,email_id,progressDialog)
                        }
                        else{
                            val message = task.exception.toString()
                            Toast.makeText(this,"Error : $message",Toast.LENGTH_SHORT).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }
    }

    private fun saveUserInfo(fullName: String, userName: String, emailId: String, progressDialog : ProgressDialog) {

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String,Any>()
        userMap["uid"] = currentUserId
        userMap["fullName"] = fullName.toLowerCase(Locale.ROOT)
        userMap["username"] = userName.toLowerCase(Locale.ROOT)
        userMap["email"] = emailId
        userMap["bio"] = "Hello There."
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/instaclone-138b8.appspot.com/o/Default%20Images%2Fman.png?alt=media&token=a3b6e6e6-8951-4120-81cc-ef64320b4778"

        usersRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(this,"User Created",Toast.LENGTH_LONG).show()

                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else{
                    val message = task.exception.toString()
                    Toast.makeText(this,"Error : $message",Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
}