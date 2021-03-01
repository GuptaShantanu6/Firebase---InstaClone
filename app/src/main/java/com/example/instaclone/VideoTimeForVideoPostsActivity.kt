package com.example.instaclone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.Model.Time
import com.example.instaclone.adapter.VideoTimeAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VideoTimeForVideoPostsActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var vidAdapter : VideoTimeAdapter? = null
    private var mTime : MutableList<Time>? = null
    private var videoId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_time_for_video_posts)

        val pref = baseContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        videoId = pref.getString("pidForProfileVideoPosts","none").toString()

        val closeBtn : ImageView = findViewById(R.id.closeActBtn)
        closeBtn.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.videoTimeRecyclerView)
        recyclerView?.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(baseContext)
        recyclerView!!.layoutManager = linearLayoutManager

        mTime = ArrayList()
        vidAdapter = baseContext?.let { VideoTimeAdapter(it,false,mTime as ArrayList<Time>) }
        recyclerView?.adapter = vidAdapter

        vidTimeInitiator()

    }

    private fun vidTimeInitiator() {
        val db = FirebaseDatabase.getInstance().reference
        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("Video Times").child(videoId.toString()).exists()){
                    mTime?.clear()
                    for (ss in snapshot.child("Video Times").child(videoId.toString()).children){
                        val p = ss.getValue(Time::class.java)
                        if (p != null){
                            mTime?.add(p)
                        }
                    }
                    vidAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}