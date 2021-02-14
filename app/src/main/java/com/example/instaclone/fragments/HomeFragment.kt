package com.example.instaclone.fragments

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeFragment : Fragment() {

    private var recyclerView : RecyclerView? = null
    private var postAdapter : PostAdapter? = null
    private var mPost : MutableList<Post>? = null

    private var currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val send : ImageView = view.findViewById(R.id.sendButton)
        send.setOnClickListener {
            Toast.makeText(activity,"Chat in Maintenance",Toast.LENGTH_SHORT).show()
        }

        val anim : LottieAnimationView = view.findViewById(R.id.homeActivityIcon)
        anim.setAnimation("mainActivityAnim.json")
        anim.playAnimation()
        anim.loop(true)

        initiator()

        recyclerView = view.findViewById(R.id.recycler_view_home) as RecyclerView
        recyclerView?.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(context)
//        linearLayoutManager.reverseLayout = true
//        linearLayoutManager.stackFromEnd = true
        recyclerView!!.layoutManager = linearLayoutManager

        mPost = ArrayList()
        postAdapter = context?.let { PostAdapter(it,true,mPost as ArrayList<Post>) }
        recyclerView?.adapter = postAdapter

        val refresh : ImageView = view.findViewById(R.id.refresh_btn)
        refresh.setOnClickListener {
            initiator()
            Toast.makeText(activity,"Refreshed",Toast.LENGTH_SHORT).show()
        }


//        checkforEmpty(view,postAdapter)

//        if (postAdapter!!.itemCount == 0){
//            val emptyAnim : LottieAnimationView = view.findViewById(R.id.emptyAnimation)
//            emptyAnim.setAnimation("629-empty-box.json")
//            emptyAnim.playAnimation()
//            emptyAnim.loop(true)
//        }

        return view

    }

    private fun initiator() {
        val checkMap = HashMap<String,Any>()
        val checkDb = FirebaseDatabase.getInstance().reference
        checkDb.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("Follow").exists()){
                    if (snapshot.child("Follow").child(currentUser!!.uid).exists()){
                        if (snapshot.child("Follow").child(currentUser!!.uid).child("Following").exists()){
                            for (ss in snapshot.child("Follow").child(currentUser!!.uid).child("Following").children){
                                checkMap[ss.key.toString()] = true
//                                Log.d("key",ss.key.toString())
//                                checkMap[getRandomString(10)] = ss.key.toString()
                            }
                        }
                    }
                }
                setValueforHomeRecyclerView(checkMap)
//                Log.d("no of kets", checkMap["X1s3g8BAJceKPIVUBkpsVXH6P1n1"].toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

//    private fun checkforEmpty(view: View?, postAdapter: PostAdapter?) {
//        if (postAdapter!!.itemCount == 0){
//            val emptyAnim : LottieAnimationView = requireView().findViewById(R.id.emptyAnimation)
//            emptyAnim.setAnimation("629-empty-box.json")
//            emptyAnim.playAnimation()
//            emptyAnim.loop(true)
//        }
//    }

    private fun setValueforHomeRecyclerView(checkMap: HashMap<String, Any>) {
//        Log.d("debug",checkMap.size.toString())
        val database = FirebaseDatabase.getInstance().reference.child("Posts").orderByChild("uploadTime")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mPost?.clear()
                for (ss in snapshot.children){
                    val i = ss.child("publisher").value.toString()
                    var flag = false
                    if (checkMap.containsKey(i)) {
                        flag = true
                    }
//                    Log.d("debug", count.toString())
                    if (flag && i!=currentUser!!.uid){
                        val p = ss.getValue(Post::class.java)
                        if (p != null){
                            mPost?.add(p)
                        }
                    }
                }
                mPost?.reverse()
                postAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
//        for (x in checkMap.keys){
//            Log.d("keys",x)
//        }
//        Log.d("size123",checkMap.size.toString())
    }

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
    }

}