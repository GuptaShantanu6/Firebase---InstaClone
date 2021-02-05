package com.example.instaclone.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.Model.Post
import com.example.instaclone.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class PostAdapter(private var mContext: Context, private var isFragment: Boolean = false, private var mPost: List<Post>)
    : RecyclerView.Adapter<PostAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.post_layout,parent,false))
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = mPost[position]
//        holder.usernameTextView.text = post.getUserName()
//        holder.postDescriptionTextView.text = post.getDescription()
//        Glide.with(holder.itemView.context).load(post.getProfileID()).into(holder.profileImageView)
//        Glide.with(holder.itemView.context).load(post.getImageID()).into(holder.postImageView)
        val pub = post.getpublisher()
//        holder.usernameTextView.text = pub
//        holder.postDescriptionTextView.text = post.getDescription()
        val pId = post.getpostId()
        val storage = FirebaseStorage.getInstance().reference

        FirebaseDatabase.getInstance().reference.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.usernameTextView.text = snapshot.child(pub).child("username").value.toString()
//                holder.postDescriptionTextView.text = holder.usernameTextView.text as String + " " + post.getDescription()
                val s = SpannableStringBuilder().bold { append(holder.usernameTextView.text.toString()) }.append(" ").append(post.getDescription())
                holder.postDescriptionTextView.text = s
            }
            override fun onCancelled(error: DatabaseError) {
                //Do Nothing till now...
            }

        })

        storage.child("Default Images").child(pub).downloadUrl.addOnSuccessListener {

            val x = it.toString()
            Glide.with(holder.itemView.context).load(x).into(holder.profileImageView)

        }.addOnFailureListener {
            Log.d("Post value error","Unable to load profile Image")
        }

        storage.child("Posts Images").child(pub).child(pId).child("Photo").downloadUrl.addOnSuccessListener {

            val x = it.toString()
            Glide.with(holder.itemView.context).load(x).into(holder.postImageView)

        }.addOnFailureListener {
            Log.d("Post Value Error","Unable to load post Image")
        }

    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    class ViewHolder(@NonNull itemView : View) : RecyclerView.ViewHolder(itemView){
        var profileImageView : CircleImageView = itemView.findViewById<CircleImageView>(R.id.user_profile_image_search)
        var usernameTextView : TextView = itemView.findViewById<TextView>(R.id.user_name_search)
        var postImageView : ImageView = itemView.findViewById<ImageView>(R.id.post_image_home)
        var postDescriptionTextView : TextView = itemView.findViewById<TextView>(R.id.post_description_home)
    }

    fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }.trimEnd()

}