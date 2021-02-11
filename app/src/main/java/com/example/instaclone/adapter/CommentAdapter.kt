package com.example.instaclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.Model.Comment
import com.example.instaclone.R
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter (private var mmContext : Context,private var isFragment : Boolean = false, private var mComment : List<Comment>)
    :RecyclerView.Adapter<CommentAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(mmContext).inflate(R.layout.comment_layout,parent,false))
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        val comment = mComment[position]
        holder.commentUsername.text = comment.getUserName()
        holder.commentDes.text = comment.getCommentDescription()
    }

    override fun getItemCount(): Int {
        return mComment.size
    }

    class ViewHolder(@NonNull itemView : View) : RecyclerView.ViewHolder(itemView){
        var profileImage : CircleImageView = itemView.findViewById(R.id.commentProfileImage)
        var commentUsername : TextView = itemView.findViewById(R.id.commentUsername)
        var commentDes : TextView = itemView.findViewById(R.id.commentDescription)
    }
}