package com.example.instaclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.instaclone.Model.NotificationC
import com.example.instaclone.R
import com.example.instaclone.adapter.NotificationsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationsFragment : Fragment() {

    private var recyclerView : RecyclerView? = null
    private var notificationAdapter : NotificationsAdapter? = null
    private var mNotification : MutableList<NotificationC>? = null

    private var currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        val notiAnim : LottieAnimationView = view.findViewById(R.id.emptyNotificationAnim)
        notiAnim.setAnimation("629-empty-box.json")

        recyclerView = view.findViewById(R.id.recycler_view_notifications) as RecyclerView
        recyclerView?.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = linearLayoutManager

        mNotification = ArrayList()
        notificationAdapter = context?.let { NotificationsAdapter(it,true,mNotification as ArrayList<NotificationC>) }
        recyclerView?.adapter = notificationAdapter

        notificationInitiator(notiAnim)

        return view
    }

    private fun notificationInitiator(notiAnim: LottieAnimationView) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("Notifications").child(currentUser!!.uid).orderByChild("uploadTime")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("Notifications").child(currentUser!!.uid).exists()){
                    for (x in snapshot.child("Notifications").child(currentUser!!.uid).children){
                        val n = x.getValue(NotificationC::class.java)
                        if (n != null){
                            mNotification?.add(n)
                        }
                    }
                    mNotification?.reverse()
                    notificationAdapter?.notifyDataSetChanged()

                }
                if (mNotification?.isEmpty() == true){
                    notiAnim.playAnimation()
                    notiAnim.loop(true)
                }
                else{
                    notiAnim.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}