package com.example.instaclone

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UploadVideoActivity : AppCompatActivity() {

    private val pickVideo = 100
    private var videoUri : Uri? = null

    private lateinit var videoView : VideoView
    private lateinit var playBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_video)

        videoView = findViewById(R.id.video_post)

        val addVideoBtn : Button = findViewById(R.id.addVideoBtn)
        addVideoBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery,pickVideo)
        }

        val closeBtn : ImageView = findViewById(R.id.close_add_video_btn)
        closeBtn.setOnClickListener {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }

        playBtn = findViewById(R.id.playBtn)
        playBtn.visibility = View.GONE

        val save : ImageView = findViewById(R.id.save_new_post_btn)
        val description: TextView = findViewById(R.id.post_description)
        val loadingAnim : LottieAnimationView = findViewById(R.id.Lottie1)

        loadingAnim.setAnimation("297-loading-rainbow.json")

        save.setOnClickListener {
            if(videoUri == null && description.text.toString().isBlank()){
                Toast.makeText(this,"Please select the video and write the description",Toast.LENGTH_SHORT).show()
            }
            else if (videoUri == null) {
                Toast.makeText(this, "Please select the video", Toast.LENGTH_SHORT).show()
            }
            else if (description.text.toString().isBlank()){
                Toast.makeText(this,"Please write the description",Toast.LENGTH_SHORT).show()
            }
            else{
                uploadVideoPostToFirebase(videoUri!!,description.text.toString(),save,loadingAnim)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadVideoPostToFirebase(videoUri: Uri, description: String, save: ImageView, loadingAnim: LottieAnimationView) {
        loadingAnim.playAnimation()
        loadingAnim.loop(true)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val x = getRandomString(28)
        val videoMap = HashMap<String,Any>()
        val db = FirebaseDatabase.getInstance().reference

        videoMap["postType"] = 2
        videoMap["publisher"] = currentUser!!.uid
        videoMap["Description"] = description
        videoMap["uploadTime"] = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().time).toString()
        videoMap["postId"] = x

        db.child("Posts").child(x).setValue(videoMap)

        val tempSt = FirebaseStorage.getInstance().reference.child("Posts Videos")
        tempSt.child(currentUser.uid).child(x).child("Video").putFile(videoUri).addOnSuccessListener {
            tempSt.child(currentUser.uid).child(x).child("Description").putStream(description.byteInputStream()).addOnSuccessListener {
                Toast.makeText(this,"Post Successfully uploaded",Toast.LENGTH_SHORT).show()
                loadingAnim.loop(false)
                startActivity(Intent(baseContext,MainActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this,"Failed to upload, Please try again",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to upload, please try again",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickVideo){
            videoUri = data?.data
            videoView.setVideoURI(videoUri)
            videoView.requestFocus()
            videoView.start()

            videoView.setOnPreparedListener {
                it.isLooping = true
                playFunction(playBtn)
            }
        }
    }

    private fun playFunction(playBtn: ImageView) {
        var aux : Int = 0
        playBtn.visibility = View.GONE
        if (videoUri != null){
            playBtn.visibility = View.VISIBLE
            playBtn.setOnClickListener {
                if (aux%2 == 0){
                    videoView.pause()
                    aux++
                }
                else{
                    videoView.start()
                    aux++
                }
            }
        }

    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
    }

    override fun onBackPressed() {

    }
}