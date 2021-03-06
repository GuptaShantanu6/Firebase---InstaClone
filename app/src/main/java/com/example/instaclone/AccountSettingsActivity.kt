package com.example.instaclone

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.HashMap

class AccountSettingsActivity : AppCompatActivity() {

    private val pickImage = 100
    private var imageUri: Uri? = null

    private lateinit var imageView : CircleImageView
    private val currentUser = FirebaseAuth.getInstance().currentUser!!
    private val storage = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)


        val close_btn : ImageView = findViewById(R.id.close_btn_account_settings)
        val logout : Button = findViewById(R.id.logout_btn_account_settings)

        val newFullName : EditText = findViewById(R.id.change_name_account_settings)
        val newUserName : EditText = findViewById(R.id.username_account_settings)
        val newBio : EditText = findViewById(R.id.bio_account_settings)

        close_btn.setOnClickListener {
            startActivity(Intent(baseContext,MainActivity::class.java))
        }


        val usernameSaveBtn : ImageView = findViewById(R.id.usernameSaveBtn)
        val fullnameSaveBtn : ImageView = findViewById(R.id.fullnameSaveBtn)
        val bioSaveBtn : ImageView = findViewById(R.id.bioSaveBtn)

        val database = getInstance().reference.child("Users").child(currentUser.uid)

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                newBio.text = Editable.Factory.getInstance().newEditable(snapshot.child("bio").value as CharSequence?)
                val temp = snapshot.child("fullName").value.toString()
                newFullName.text = Editable.Factory.getInstance().newEditable(temp.capitalizeFirstLetter())
                newUserName.text = Editable.Factory.getInstance().newEditable(snapshot.child("username").value as CharSequence?)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        fullnameSaveBtn.setOnClickListener {
            val f = newFullName.text.toString()
            val newMap = HashMap<String,Any>()
            newMap["fullName"] = f.toLowerCase(Locale.ROOT)
            database.updateChildren(newMap)
            Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
        }

        usernameSaveBtn.setOnClickListener {
            val u = newUserName.text.toString()
                val newMap = HashMap<String,Any>()
                newMap["username"] = u.toLowerCase(Locale.ROOT)
                database.updateChildren(newMap)
                Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
        }

        bioSaveBtn.setOnClickListener {
            val newMap = HashMap<String,Any>()
            val b = newBio.text.toString()
            newMap["bio"] = b
            database.updateChildren(newMap)
            Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
        }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this,"Successfully Logged Out",Toast.LENGTH_SHORT).show()
            backToMain()

        }

        val deleteAct_btn : Button = findViewById<Button>(R.id.delete_account_btn_account_settings)
        deleteAct_btn.setOnClickListener {

        }

        val imageSelect : TextView = findViewById<Button>(R.id.change_image_btn)
        imageSelect.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,pickImage)
        }

        imageView = findViewById(R.id.profile_imag_account_settings)
        checkForProfileImage(currentUser,storage,imageView,database)

    }

    private fun checkForProfileImage(currentUser: FirebaseUser, storage: StorageReference, imageView: CircleImageView?, database: DatabaseReference) {
        storage.child("Default Images/").child(currentUser.uid).downloadUrl.addOnSuccessListener {
            val newMap = HashMap<String,Any>()
            newMap["image"] = it.toString()
            database.updateChildren(newMap)
            val x = it.toString()
            if (imageView != null) {
                Glide.with(this)
                    .load(x)
                    .into(imageView)
            }
        }.addOnFailureListener {
            //Do Nothing , i.e. leave the default image
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage){
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            uploadImageToFirebase(imageUri,storage)

        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?, storage: StorageReference) {
        storage.child("Default Images/").child(currentUser.uid+".png").downloadUrl
                .addOnSuccessListener {
                    storage.child("Default Images/").child(currentUser.uid).delete()
                    if (imageUri != null) {
                        storage.child("Default Images/").child(currentUser.uid).putFile(imageUri)
                    }
                }.addOnFailureListener {
                    if (imageUri != null) {
                        storage.child("Default Images/").child(currentUser.uid).putFile(imageUri).addOnSuccessListener {
                            Toast.makeText(this,"Image Successfully Uploaded",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this,"Image Upload failed. Please try again",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
         }

    private fun backToMain() {
        val intent = Intent(this@AccountSettingsActivity,SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }.trimEnd()
}