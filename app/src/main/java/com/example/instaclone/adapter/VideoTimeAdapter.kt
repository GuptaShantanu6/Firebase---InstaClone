package com.example.instaclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.Model.Time
import com.example.instaclone.R

class VideoTimeAdapter(private var mContext: Context, private var isFragment : Boolean = false, private var mTime : List<Time>)
    : RecyclerView.Adapter<VideoTimeAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoTimeAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.individual_time_layout,parent,false))
    }

    override fun onBindViewHolder(holder: VideoTimeAdapter.ViewHolder, position: Int) {
        val time = mTime[position]
        holder.timeTextView.text = time.getTime()
    }

    override fun getItemCount(): Int {
        return mTime.size
    }

    class ViewHolder(@NonNull itemView : View) : RecyclerView.ViewHolder(itemView){
        val timeTextView : TextView = itemView.findViewById(R.id.single_time)

    }

}