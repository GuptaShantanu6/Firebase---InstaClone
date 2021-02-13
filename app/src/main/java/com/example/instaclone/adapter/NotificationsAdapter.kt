package com.example.instaclone.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.Model.NotificationC
import com.example.instaclone.R
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class NotificationsAdapter(private var mContext : Context, private var isFragment : Boolean = true, private var mNotification : List<NotificationC>)
    :RecyclerView.Adapter<NotificationsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notification_layout,parent,false))
    }

    override fun onBindViewHolder(holder: NotificationsAdapter.ViewHolder, position: Int) {
        val notification = mNotification[position]
        val notiType = notification.getType()
        if (notiType == "Other"){
            holder.notificationText.text = SpannableStringBuilder().bold { append(notification.getUserName()) }.append(" started following you")
        }
        else{
            holder.notificationText.text = SpannableStringBuilder().append("You started following ").bold { notification.getUserName() }
        }

        val storage = FirebaseStorage.getInstance().reference.child("Default Images")
        storage.child(notification.getUserId()).downloadUrl.addOnSuccessListener {
            val x = it.toString()
            Glide.with(holder.itemView.context).load(x).into(holder.userNotiImag)
        }
    }

    override fun getItemCount(): Int {
        return mNotification.size
    }

    class ViewHolder(@NonNull itemView : View) : RecyclerView.ViewHolder(itemView){
        val userNotiImag : CircleImageView = itemView.findViewById(R.id.followUserImag)
        val notificationText : TextView = itemView.findViewById(R.id.notificationText)
    }

}