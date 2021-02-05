package com.example.instaclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.instaclone.Model.Post
import com.example.instaclone.R
import com.example.instaclone.adapter.PostAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var recyclerView : RecyclerView? = null
    private var postAdapter : PostAdapter? = null
    private var mPost : MutableList<Post>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val send : ImageView = view.findViewById<ImageView>(R.id.sendButton)
        send.setOnClickListener {
            Toast.makeText(activity,"Chat in Maintaenance",Toast.LENGTH_SHORT).show()
        }

        val anim : LottieAnimationView = view.findViewById<LottieAnimationView>(R.id.homeActivityIcon)
        anim.setAnimation("mainActivityAnim.json")
        anim.playAnimation()
        anim.loop(true)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_home) as RecyclerView
        recyclerView?.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView!!.layoutManager = linearLayoutManager

        mPost = ArrayList()
        postAdapter = context?.let { PostAdapter(it,true,mPost as ArrayList<Post>) }
        recyclerView?.adapter = postAdapter

        setValueforHomeRecyclerView()

        return view
    }

    private fun setValueforHomeRecyclerView() {
        val database = FirebaseDatabase.getInstance().reference.child("Posts").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mPost?.clear()
                for (ss in snapshot.children){
                    val p = ss.getValue(Post::class.java)
                    if (p != null){
                        mPost?.add(p)
                    }
                }
                postAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}