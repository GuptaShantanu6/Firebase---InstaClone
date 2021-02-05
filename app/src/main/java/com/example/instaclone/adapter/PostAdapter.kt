package com.example.instaclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.Model.Post
import com.example.instaclone.R
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
        holder.usernameTextView.text = post.getpublisher()
        holder.postDescriptionTextView.text = post.getDescription()

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