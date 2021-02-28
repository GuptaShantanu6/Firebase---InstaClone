package com.example.instaclone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.annotation.NonNull
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.instaclone.Model.Post
import com.example.instaclone.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class PostAdapterForProfile(private var mContext: Context, private var isFragment: Boolean = true, private var mPost: List<Post>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    class ImageViewHolder(@NonNull itemView : View) : RecyclerView.ViewHolder(itemView){
        val userImage : CircleImageView = itemView.findViewById(R.id.user_profile_image_search_profile)
        val username : TextView = itemView.findViewById(R.id.user_name_search_profile)
        val postImage : ImageView = itemView.findViewById(R.id.post_image_home_profile)
        val likesTextView : TextView = itemView.findViewById(R.id.likes_profile)
        val description : TextView = itemView.findViewById(R.id.post_description_profile)

        fun bind(position : Int, mPost: List<Post>){
            val post = mPost[position]
            val pubProfile = post.getpublisher()
            val pidProfile = post.getpostId()

            FirebaseDatabase.getInstance().reference
                    .child("Users").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    username.text = snapshot.child(pubProfile).child("username").value.toString()
                    description.text = SpannableStringBuilder().bold { append(username.text) }.append(" ${post.getDescription()}")
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

            FirebaseStorage.getInstance().reference
                    .child("Default Images")
                    .child(pubProfile).downloadUrl.addOnSuccessListener {
                        val x = it.toString()
                        Glide.with(itemView.context).load(x).into(userImage)
                    }

            FirebaseStorage.getInstance().reference
                    .child("Posts Images")
                    .child(pubProfile)
                    .child(pidProfile).child("Photo").downloadUrl.addOnSuccessListener {
                        val x = it.toString()
                        Glide.with(itemView.context).load(x).into(postImage)
                    }

            FirebaseDatabase.getInstance().reference.addValueEventListener(object : ValueEventListener{
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    likesTextView.text = snapshot.child(pidProfile).childrenCount.toString() + " Likes"
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }
    }

    class VideoViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        val userImage : CircleImageView = itemView.findViewById(R.id.user_profile_image_search_video_post_profile)
        val username : TextView = itemView.findViewById(R.id.user_name_search_video_post_profile)
        val videoView : VideoView = itemView.findViewById(R.id.post_video_profile)
        val likesTextView : TextView = itemView.findViewById(R.id.likes_video_profile)
        val videoDescription : TextView = itemView.findViewById(R.id.post_description_video_profile)
        val loadingAnim : LottieAnimationView = itemView.findViewById(R.id.videoAnimLoading_profile)
        val playBtn : ImageView = itemView.findViewById(R.id.play_btn_profile)

        fun bind (position: Int, mPost: List<Post>){
            val post = mPost[position]
            val pubProfile = post.getpublisher()
            val pidProfile = post.getpostId()

            loadingAnim.visibility = View.VISIBLE
            playBtn.visibility = View.GONE

            loadingAnim.playAnimation()
            loadingAnim.loop(true)

            FirebaseStorage.getInstance().reference.child("Default Images").child(pubProfile).downloadUrl.addOnSuccessListener {
                val x = it.toString()
                Glide.with(itemView.context).load(x).into(userImage)
            }

            FirebaseDatabase.getInstance().reference.child("Users").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    username.text = snapshot.child("username").value.toString()
                    videoDescription.text = SpannableStringBuilder().bold { append(username.text) }.append(" ").append(post.getDescription())
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

            FirebaseStorage.getInstance().reference.child("Posts Videos").child(pubProfile).child(pidProfile).child("Video").downloadUrl
                    .addOnSuccessListener {
                        val x = it.toString()
                        videoView.setVideoURI(Uri.parse(x))
                    }

            videoView.requestFocus()
            videoView.start()
            videoView.setOnPreparedListener {
                loadingAnim.loop(false)
                loadingAnim.visibility = View.GONE
                it.isLooping = true
                it.setVolume(0f,0f)
                playBtn.visibility = View.VISIBLE
            }

            playBtn.setOnClickListener {
                if (videoView.isPlaying){
                    videoView.pause()
                    playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                else{
                    videoView.start()
                    playBtn.setImageResource(R.drawable.ic_pause)
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ONE){
            ImageViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.post_layout_profile,parent,false)
            )
        }
        else{
            VideoViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.video_post_layout_profile,parent,false)
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