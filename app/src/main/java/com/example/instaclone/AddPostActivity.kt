package com.example.instaclone

import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.content.Context
import android.view.inputmethod.InputMethodManager;



class AddPostActivity : AppCompatActivity() {

    private val pickImage = 100
    private var imageUri: Uri? = null

    private val currentUser = FirebaseAuth.getInstance().currentUser!!
    private val storage = FirebaseStorage.getInstance().reference

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val close: ImageView = findViewById<ImageView>(R.id.close_add_post_btn)
        close.setOnClickListener {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }

        val addPictureBtn: Button = findViewById<Button>(R.id.AddPictureBtn)
        imageView = findViewById<ImageView>(R.id.image_post)

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
                uploadImagePostToFirebaseStorage(imageUri!!, storage, currentUser,description.text.toString())
//                val x = getRandomString(2)
            }

        }
    }

    private fun uploadImagePostToFirebaseStorage(imageUri: Uri, storage: StorageReference, currentUser: FirebaseUser,description : String) {
        val x = getRandomString(28)
//        var a : Int = 0
//        storage.child("Posts Images").child(currentUser.uid.toString()).child(x).child("Photo").putFile(imageUri).addOnSuccessListener {
//            a+=1
//        }.addOnFailureListener {
//
//        }
//        storage.child("Posts Images").child(currentUser.uid.toString()).child(x).child("Description").putStream(description.byteInputStream()).addOnSuccessListener {
//            a+=1
//        }.addOnFailureListener {
//
//        }
//        if (a==2){
//            Toast.makeText(this,"Post Uploaded Successfully",Toast.LENGTH_SHORT).show()
//            startActivity(Intent(baseContext,MainActivity::class.java))
//        }
//        else{
//            Toast.makeText(this,"Error in Uploading, Please try again",Toast.LENGTH_SHORT).show()
//        }
        storage.child("Posts Images").child(currentUser.uid.toString()).child(x).child("Photo").putFile(imageUri).addOnSuccessListener {
            storage.child("Posts Images").child(currentUser.uid.toString()).child(x).child("Description").putStream(description.byteInputStream()).addOnSuccessListener {
                Toast.makeText(this,"Post Successfully uploaded",Toast.LENGTH_SHORT).show()
                startActivity(Intent(baseContext,MainActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this,"Error, Please try again",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }

    }

    fun getRandomString(length: Int): String {
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
