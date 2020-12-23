package com.example.instaclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.Model.User
import com.example.instaclone.R
import com.squareup.picasso.Picasso

class UserAdapter (private var mContext : Context, private var mUser : List<User>, private var isFragment : Boolean = false)
    : RecyclerView.Adapter<UserAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
//        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false)
//        return UserAdapter.ViewHolder(view)

        val h = ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false))
        return h
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = mUser[position]
        holder.usernameTextView.text = user.getUserName()
        holder.fullnameTextView.text = user.getFullName()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.ic_man).into(holder.profileImageSearchView)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    class ViewHolder(@NonNull itemView : View ) : RecyclerView.ViewHolder(itemView){
        var usernameTextView : TextView = itemView.findViewById<TextView>(R.id.user_name_search)
        var fullnameTextView : TextView = itemView.findViewById<TextView>(R.id.user_full_name_search)
        var profileImageSearchView : ImageView = itemView.findViewById<ImageView>(R.id.user_profile_image_search)
        var followButton : Button = itemView.findViewById<Button>(R.id.following_btn)
    }


}