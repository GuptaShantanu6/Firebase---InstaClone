package com.example.instaclone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.instaclone.CommentsActivity
import com.example.instaclone.Model.Post
import com.example.instaclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.nio.InvalidMarkException
import kotlin.concurrent.thread

class PostAdapter(private var mContext: Context, private var isFragment: Boolean = true, private var mPost: List<Post>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    class ImageViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var profileImageView : CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
        var usernameTextView : TextView = itemView.findViewById(R.id.user_name_search)
        var postImageView : ImageView = itemView.findViewById(R.id.post_image_home)
        var postDescriptionTextView : TextView = itemView.findViewById(R.id.post_description_home)
        var like : ImageView = itemView.findViewById(R.id.post_image_likebtn)
        var likesView : TextView = itemView.findViewById(R.id.likes)
        var commentsView : TextView = itemView.findViewById(R.id.comments)
        val commentBtn : ImageView = itemView.findViewById(R.id.post_image_comment_btn)


        @SuppressLint("CommitPrefEdits")
        fun bind(position: Int, mPost: List<Post>){
            val post = mPost[position]
            val pub = post.getpublisher()
            val pId = post.getpostId()
            FirebaseDatabase.getInstance().reference.child("Users").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    usernameTextView.text = snapshot.child(pub).child("username").value.toString()
                    postDescriptionTextView.text = SpannableStringBuilder().bold { append(usernameTextView.text) }.append(" ${post.getDescription()}")
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

            FirebaseStorage.getInstance().reference.child("Default Images")
                    .child(pub).downloadUrl.addOnSuccessListener {
                        val x = it.toString()
                        Glide.with(itemView.context).load(x).into(profileImageView)
                    }.addOnFailureListener {
                        Log.d("storage error","Default Images storage error")
                    }

            FirebaseStorage.getInstance().reference.child("Posts Images")
                    .child(pub).child(pId).child("Photo").downloadUrl.addOnSuccessListener {
                        val x = it.toString()
                        Glide.with(itemView.context).load(x).into(postImageView)
                    }.addOnFailureListener {
                        Log.d("storage error","posts images storage error")
                    }

            commentsView.setOnClickListener {
                val whichPost = itemView.context.getSharedPreferences("whichIDs",Context.MODE_PRIVATE).edit()
                whichPost.apply{
                    putString("postID",pId)
                    putString("postPublisher",pub)
                    apply()
                }

                val intent = Intent(itemView.context, CommentsActivity::class.java)
                itemView.context.startActivity(intent)
            }

            val currentUser = FirebaseAuth.getInstance().currentUser
            val likeDb = FirebaseDatabase.getInstance().reference

            var check = false

            likeDb.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("Like").child(pId).child(currentUser!!.uid).exists()){
                        like.setImageResource(R.drawable.ic_heart_filled)
                        check = true
                        @SuppressLint("SetTextI18n")
                        likesView.text = snapshot.child("Like").child(pId).childrenCount.toString() + " Likes"
                    }
                    else{
                        like.setImageResource(R.drawable.ic_heart)
                        @SuppressLint("SetTextI18n")
                        likesView.text = snapshot.child("Like").child(pId).childrenCount.toString() + " Likes"
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

            like.setOnClickListener {
                check = if (check){
                    like.setImageResource(R.drawable.ic_heart)
                    likeDb.child("Like").child(pId).child(currentUser!!.uid).removeValue()
                    likeDb.child("Like").child(pId).addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            @SuppressLint("SetTextI18n")
                            likesView.text = snapshot.childrenCount.toString() + " Likes"
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
                    false
                } else {
                    like.setImageResource(R.drawable.ic_heart_filled)
                    likeDb.child("Like").child(pId).child(currentUser!!.uid).setValue(true)
                    likeDb.child("Like").child(pId).addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            @SuppressLint("SetTextI18n")
                            likesView.text = snapshot.childrenCount.toString() + " Likes"
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
                    true
                }
            }

        }
    }

    class VideoViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        val videoView : VideoView = itemView.findViewById(R.id.post_video_home)
        val usernameTextView : TextView = itemView.findViewById(R.id.user_name_search)
        val profileImageView : CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
        val postDescriptionTextView : TextView = itemView.findViewById(R.id.post_description_home)
        val videoLikes : TextView = itemView.findViewById(R.id.video_likes)
        val commentsView : TextView = itemView.findViewById(R.id.comments)
        val likeBtn : ImageView = itemView.findViewById(R.id.video_likeBtn)
        val anim : LottieAnimationView = itemView.findViewById(R.id.videoAnimLoading)
        val mcButton : ImageView = itemView.findViewById(R.id.mcButton)
        val playBtn : ImageView = itemView.findViewById(R.id.play_btn)

        fun bind(position: Int, mPost: List<Post>){
            val post = mPost[position]
            val pId = post.getpostId()
            val pub = post.getpublisher()

            Log.d("pId 22",pId)
            Log.d("pub 22",pub)

            anim.setAnimation("297-loading-rainbow.json")
            anim.visibility = View.VISIBLE
            anim.playAnimation()
            anim.loop(true)

            commentsView.setOnClickListener {
                val whichPost = itemView.context.getSharedPreferences("whichIDs",Context.MODE_PRIVATE).edit()
                whichPost.apply{
                    putString("postID",pId)
                    apply()
                }
                Log.d("posID_from_videoAdapter",pId)

                val intent = Intent(itemView.context,CommentsActivity::class.java)
                itemView.context.startActivity(intent)


            }


            FirebaseDatabase.getInstance().reference.child("Users").child(pub).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    usernameTextView.text = snapshot.child("username").value.toString()
                    postDescriptionTextView.text = SpannableStringBuilder().bold { append(usernameTextView.text) }.append(" ${post.getDescription()}")
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

            val storage = FirebaseStorage.getInstance().reference
            storage.child("Default Images").child(pub).downloadUrl.addOnSuccessListener {
                val x = it.toString()
                Glide.with(itemView.context).load(x).into(profileImageView)
            }.addOnFailureListener {
                Log.d("storage error","default images storage error")
            }

            playBtn.visibility = View.GONE

            val mediaController = MediaController(itemView.context)
            storage.child("Posts Videos").child(pub).child(pId).child("Video").downloadUrl.addOnSuccessListener {
                val x = it.toString()
                videoView.setMediaController(mediaController)
                videoView.setVideoURI(Uri.parse(x))
            }.addOnFailureListener {
                Log.d("storage error","Posts Videos storage error")
            }
            videoView.requestFocus()
            videoView.start()
            mediaController.setAnchorView(videoView)
            videoView.setOnPreparedListener { xx ->
                anim.loop(false)
                anim.visibility = View.GONE
                playBtn.visibility = View.VISIBLE
                xx.isLooping = true
                xx.setVolume(0f,0f)
            }

            mediaController.visibility = View.GONE

//            mcButton.setOnClickListener {
//                mediaController.show()
//            }

            var tm : String? = null
            val db = FirebaseDatabase.getInstance().reference.child("Video Times").child(pId)
            var check = false

//
//            playBtn.setOnClickListener {
//                if (videoView.isPlaying){
//                    videoView.pause()
//                    playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
//                    tm = getVideoTime(videoView.currentPosition)
////                    Toast.makeText(itemView.context,tm,Toast.LENGTH_SHORT).show()
//                    if (!check){
//                        db.child(getRandomString(5)).child("time").setValue(tm)
//                                .addOnSuccessListener {
//                                    check = true
//                                }.addOnFailureListener {
//                                    Log.d("Time Error","Unable to save time stamp")
//                                }
//                    }
//                }
//                else{
//                    videoView.start()
//                    playBtn.setImageResource(R.drawable.ic_pause)
//                }
//            }

            playBtn.setOnClickListener {
                if (videoView.isPlaying){
                    videoView.pause()
                    playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    tm = getVideoTime(videoView.currentPosition)
                    val job = GlobalScope.launch(Dispatchers.Default) {
                        db.child(getRandomString(5)).child("time").setValue(tm)
                    }
                    runBlocking {
                        job.join()
                    }
                }
                else {
                    videoView.start()
                    playBtn.setImageResource(R.drawable.ic_pause)
                }
            }

//            if (tm != null){
//                Toast.makeText(itemView.context,tm,Toast.LENGTH_SHORT).show()
//            }

            val likeDb = FirebaseDatabase.getInstance().reference
            val currentUser = FirebaseAuth.getInstance().currentUser

            var videoCheck = false

            likeDb.addValueEventListener(object : ValueEventListener{
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                     if (snapshot.child("Like").child(pId).child(currentUser!!.uid).exists()){
                        likeBtn.setImageResource(R.drawable.ic_heart_filled)
                        videoCheck = true
                    } else {
                        likeBtn.setImageResource(R.drawable.ic_heart)
                    }
                    videoLikes.text = snapshot.child("Like").child(pId).childrenCount.toString() + " Likes"
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

            likeBtn.setOnClickListener {
                if (videoCheck){
                    likeBtn.setImageResource(R.drawable.ic_heart)
                    likeDb.child("Like").child(pId).child(currentUser!!.uid).removeValue()
                    likeDb.addValueEventListener(object : ValueEventListener{
                        @SuppressLint("SetTextI18n")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            videoLikes.text = snapshot.child("Like").child(pId).childrenCount.toString() + "Likes"
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
                    videoCheck = false
                }
                else {
                    likeBtn.setImageResource(R.drawable.ic_heart_filled)
                    likeDb.child("Like").child(pId).child(currentUser!!.uid).setValue(true)
                    likeDb.addValueEventListener(object : ValueEventListener{
                        @SuppressLint("SetTextI18n")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            videoLikes.text = snapshot.child("Like").child(pId).childrenCount.toString() + "Likes"
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
                    videoCheck = true
                }
            }



        }

        private fun getRandomString(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                    .map { allowedChars.random() }
                    .joinToString("")
        }

        private fun getVideoTime(ms: Int) : String{
            val temp = ms/1000
            return SpannableStringBuilder().append((temp/3600).toString()+":"+((temp%360)/60)+":"+((temp%3600)%60)).toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ONE){
            ImageViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.post_layout,parent,false)
            )
        }
        else {
            VideoViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.video_post_layout,parent,false)
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mPost[position].getPostType() == VIEW_TYPE_ONE){
            (holder as ImageViewHolder).bind(position,mPost)
        }
        else{
            (holder as VideoViewHolder).bind(position,mPost)
        }
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun getItemViewType(position: Int): Int {
        return mPost[position].getPostType()
    }


}

