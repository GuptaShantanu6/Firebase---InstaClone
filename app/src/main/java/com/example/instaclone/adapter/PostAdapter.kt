package com.example.instaclone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings.Global.putString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.annotation.NonNull
import androidx.core.text.bold
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
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

class PostAdapter(private var mContext: Context, private var isFragment: Boolean = true, private var mPost: List<Post>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.post_layout,parent,false))
//    }
//
//    @SuppressLint("CommitPrefEdits", "SetTextI18n")
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val post = mPost[position]
//        val pub = post.getpublisher()
//        val pId = post.getpostId()
//        val storage = FirebaseStorage.getInstance().reference
//
//        FirebaseDatabase.getInstance().reference.child("Users").addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                holder.usernameTextView.text = snapshot.child(pub).child("username").value.toString()
//                val s = SpannableStringBuilder().bold { append(holder.usernameTextView.text.toString()) }.append(" ").append(post.getDescription())
//                holder.postDescriptionTextView.text = s
//            }
//            override fun onCancelled(error: DatabaseError) {
//                //Do Nothing till now...
//            }
//
//        })
//
//        storage.child("Default Images").child(pub).downloadUrl.addOnSuccessListener {
//
//            val x = it.toString()
//            Glide.with(holder.itemView.context).load(x).into(holder.profileImageView)
//
//        }.addOnFailureListener {
//            Log.d("Post value error","Unable to load profile Image")
//        }
//
//        storage.child("Posts Images").child(pub).child(pId).child("Photo").downloadUrl.addOnSuccessListener {
//
//            val x = it.toString()
//            Glide.with(holder.itemView.context).load(x).into(holder.postImageView)
//
//        }.addOnFailureListener {
//            Log.d("Post Value Error","Unable to load post Image")
//        }
//
//        var check = false
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        val likeDb = FirebaseDatabase.getInstance().reference
//        likeDb.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.child("Like").child(pId).child(currentUser!!.uid).exists()){
//                    holder.like.setImageResource(R.drawable.ic_heart_filled)
//                    check = true
//                    @SuppressLint("SetTextI18n")
//                    holder.likesView.text = snapshot.child("Like").child(pId).childrenCount.toString() + " Likes"
//                }
//                else{
//                    holder.like.setImageResource(R.drawable.ic_heart)
//                    @SuppressLint("SetTextI18n")
//                    holder.likesView.text = snapshot.child("Like").child(pId).childrenCount.toString() + " Likes"
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//
//        holder.like.setOnClickListener {
//            check = if (check){
//                holder.like.setImageResource(R.drawable.ic_heart)
//                likeDb.child("Like").child(pId).child(currentUser!!.uid).removeValue()
//                likeDb.child("Like").child(pId).addValueEventListener(object : ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        @SuppressLint("SetTextI18n")
//                        holder.likesView.text = snapshot.childrenCount.toString() + " Likes"
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//
//                })
//                false
//            } else {
//                holder.like.setImageResource(R.drawable.ic_heart_filled)
//                likeDb.child("Like").child(pId).child(currentUser!!.uid).setValue(true)
//                likeDb.child("Like").child(pId).addValueEventListener(object : ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        @SuppressLint("SetTextI18n")
//                        holder.likesView.text = snapshot.childrenCount.toString() + " Likes"
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//
//                })
//                true
//            }
//        }
//
//        holder.commentsView.setOnClickListener {
//            val whichPost = mContext.getSharedPreferences("whichIDs",Context.MODE_PRIVATE).edit()
//            whichPost.apply{
//                putString("postID",pId)
//                putString("postPublisher",pub)
//                apply()
//            }
//
//            val intent = Intent(mContext,CommentsActivity::class.java)
//            mContext.startActivity(intent)
//        }
//        holder.commentsView.text = "View All Comments"
//
//    }
//
//    override fun getItemCount(): Int {
//        return mPost.size
//    }
//
//    class ViewHolder(@NonNull itemView : View) : RecyclerView.ViewHolder(itemView){
//        var profileImageView : CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
//        var usernameTextView : TextView = itemView.findViewById(R.id.user_name_search)
//        var postImageView : ImageView = itemView.findViewById(R.id.post_image_home)
//        var postDescriptionTextView : TextView = itemView.findViewById(R.id.post_description_home)
//        var like : ImageView = itemView.findViewById(R.id.post_image_likebtn)
//        var likesView : TextView = itemView.findViewById(R.id.likes)
//        var commentsView : TextView = itemView.findViewById(R.id.comments)
//        val commentBtn : ImageView = itemView.findViewById(R.id.post_image_comment_btn)
//    }
//
//    fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }.trimEnd()

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
                    }

            FirebaseStorage.getInstance().reference.child("Posts Images")
                    .child(pub).child(pId).child("Photo").downloadUrl.addOnSuccessListener {
                        val x = it.toString()
                        Glide.with(itemView.context).load(x).into(postImageView)
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
        val likesView : TextView = itemView.findViewById(R.id.likes)
        val commentsView : TextView = itemView.findViewById(R.id.comments)
        val likeBtn : ImageView = itemView.findViewById(R.id.post_image_likebtn)

        fun bind(position: Int, mPost: List<Post>){
            val post = mPost[position]
            val pId = post.getpostId()
            val pub = post.getpublisher()

            Log.d("pId 22",pId)
            Log.d("pub 22",pub)


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
            }
            storage.child("Posts Videos").child(pub).child(pId).child("Video").downloadUrl.addOnSuccessListener {
                val x = it.toString()
                videoView.setVideoURI(Uri.parse(x))
            }
            videoView.requestFocus()
            videoView.start()
            videoView.setOnPreparedListener { xx ->
                xx.isLooping = true
                xx.setVolume(0f,0f)
            }
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

