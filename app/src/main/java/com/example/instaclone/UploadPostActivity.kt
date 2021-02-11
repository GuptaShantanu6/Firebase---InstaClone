package com.example.instaclone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class UploadPostActivity : AppCompatActivity() {

    private val pickImage = 100
    private var imageUri: Uri? = null

    private val currentUser = FirebaseAuth.getInstance().currentUser!!
    private val storage = FirebaseStorage.getInstance().reference

    private lateinit var imageView: ImageView

    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_post)

        val close: ImageView = findViewById<ImageView>(R.id.close_add_post_btn)
        close.setOnClickListener {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }

//        val debug : Button = findViewById<Button>(R.id.databaseBtn)
//        debug.setOnClickListener {
////            val p = HashMap<String,Any>()
////            p["temp"]="temp"
////            database.child("Posts").child(getRandomString(5)).setValue(p)
////            Toast.makeText(this,"Temporary Update to realtime db",Toast.LENGTH_SHORT).show()
//            val d = Calendar.getInstance()
//            val sdfr : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
//            val now = d.time
//            val dater = sdfr.format(now)
//            Toast.makeText(this,dater.toString(),Toast.LENGTH_LONG).show()
//
//
//        }

        val captureAnim : LottieAnimationView = findViewById<LottieAnimationView>(R.id.captureAnim)
        captureAnim.setAnimation("capture.json")
        captureAnim.playAnimation()
        captureAnim.loop(true)

        val addPictureBtn: Button = findViewById<Button>(R.id.AddPictureBtn)
        imageView = findViewById<ImageView>(R.id.image_post)


        addPictureBtn.isClickable = true
        addPictureBtn.isEnabled = true

        addPictureBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        val description: TextView = findViewById<TextView>(R.id.post_description)

        val save: ImageView = findViewById<ImageView>(R.id.save_new_post_btn)
        save.setOnClickListener {
            if (imageUri == null || description.text.toString().isBlank()) {
                if (imageUri == null && description.text.toString().isBlank()) {
                    Toast.makeText(this, "Please select the image and write the description", Toast.LENGTH_SHORT).show()
                } else {
                    if (imageUri == null) {
                        Toast.makeText(this, "Please select the image", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Please write the description", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                //Upload the image
                //Functionality to be made
                captureAnim.loop(false)
                uploadImagePostToFirebaseStorage(imageUri!!, storage, currentUser,description.text.toString(),addPictureBtn)
//                val x = getRandomString(2)
            }

        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadImagePostToFirebaseStorage(imageUri: Uri, storage: StorageReference, currentUser: FirebaseUser, description: String, addPictureBtn: Button) {
        val x = getRandomString(28)
        val lottie : LottieAnimationView = findViewById<LottieAnimationView>(R.id.Lottie1)

        lottie.setAnimation("297-loading-rainbow.json")
        lottie.bringToFront()

        val postMap = HashMap<String,Any>()
        postMap["Description"] = description
        postMap["postId"] = x
        postMap["publisher"] = currentUser.uid
        postMap["uploadTime"] = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().time).toString()
        database.child("Posts").child(x).setValue(postMap)

        addPictureBtn.isEnabled = false
        addPictureBtn.isClickable = false

        lottie.playAnimation()
        lottie.loop(true)

        val tempST = storage.child("Posts Images").child(currentUser.uid).child(x)

        tempST.child("Photo").putFile(imageUri).addOnSuccessListener {
            tempST.child("Description").putStream(description.byteInputStream()).addOnSuccessListener {
//                postMap["postImage"] = storage.child("Posts Images").child(currentUser.uid).child(x).child("Photo").downloadUrl.toString()

//                tempST.child("Photo").downloadUrl.let { task ->
//                    val temp = task.result.toString()
//                    postMap["postImage"] = temp
//                }
//                val newMap = HashMap<String,Any>()
//                newMap["postImage"] = tempST.child("Description").downloadUrl.result.toString()
//                database.child("Posts").child(x).updateChildren(newMap)

//                database.child("Posts").child(x).setValue(postMap)

                lottie.loop(false)

                Toast.makeText(this,"Post Successfully uploaded",Toast.LENGTH_SHORT).show()
                startActivity(Intent(baseContext,MainActivity::class.java))

            }.addOnFailureListener {
                Toast.makeText(this,"Error, Please try again",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Post Upload Failed, Please Try Again",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }

    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
    }
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}