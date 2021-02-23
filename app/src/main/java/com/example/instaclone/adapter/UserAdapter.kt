package com.example.instaclone.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.AccountSettingsActivity
import com.example.instaclone.Model.User
import com.example.instaclone.R
import com.example.instaclone.UserClickFromSearchActivity
import com.example.instaclone.adapter.UserAdapter.ViewHolder
import com.example.instaclone.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UserAdapter (private var mContext : Context, private var mUser : List<User>, private var isFragment : Boolean = false)
    : RecyclerView.Adapter<ViewHolder>()
{

    private var firebaseUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val storage = FirebaseStorage.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {

        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUser[position]
        holder.usernameTextView.text = user.getUserName()
        holder.fullnameTextView.text = user.getFullName().capitalizeFirstLetter()
        storage.child("Default Images").child(user.getUID()).downloadUrl.addOnSuccessListener {
            val x = it.toString()
            Glide.with(mContext)
                    .load(x)
                    .into(holder.profileImageSearchView)
        }.addOnFailureListener {

        }

        checkFollowingStatus(user.getUID(),holder.followButton)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit()
            pref.apply{
                putString("profileID",user.getUID())
                apply()
            }
            if (user.getUID() != firebaseUser?.uid){
                val context = mContext
                val intent = Intent(context,UserClickFromSearchActivity::class.java)
                context.startActivity(intent)
            }

        })

        if (user.getUID() == firebaseUser?.uid){
            holder.followButton.visibility = View.GONE
        }
        else{
            holder.followButton.visibility = View.VISIBLE
            holder.followButton.setOnClickListener {
                if (holder.followButton.text.toString() == "Follow") {
                    firebaseUser?.uid.let {
                        FirebaseDatabase.getInstance().reference
                                .child("Follow").child(it.toString())
                                .child("Following").child(user.getUID())
                                .setValue(true).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        firebaseUser?.uid.let { it ->
                                            FirebaseDatabase.getInstance().reference
                                                    .child("Follow").child(user.getUID())
                                                    .child("Followers").child(it.toString())
                                                    .setValue(true).addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            val notMap = HashMap<String,Any>()
                                                            notMap["status"] = "youTrue"
                                                            notMap["otherUser"] = user.getUserName()
                                                            notMap["uploadTime"] = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().time).toString()
                                                            notMap["userId"] = user.getUID()
                                                            FirebaseDatabase.getInstance().reference
                                                                    .child("Notifications")
                                                                    .child(firebaseUser!!.uid)
                                                                    .child(getRandomString(5)).setValue(notMap)

                                                            val notMap2 = HashMap<String,Any>()
                                                            notMap2["status"] = "otherTrue"

                                                            var x = ""
                                                            FirebaseDatabase.getInstance().reference.child("Users").addValueEventListener(object : ValueEventListener{
                                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                                    x = snapshot.child(firebaseUser!!.uid).child("username").value.toString()
                                                                    notMap2["otherUser"] = x
                                                                    notMap2["uploadTime"] = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().time).toString()
                                                                    notMap2["userId"] = user.getUID()
                                                                    FirebaseDatabase.getInstance().reference
                                                                            .child("Notifications")
                                                                            .child(user.getUID())
                                                                            .child(getRandomString(5)).setValue(notMap2)

                                                                }

                                                                override fun onCancelled(error: DatabaseError) {

                                                                }

                                                            })


                                                        }
                                                    }

                                        }
                                    }
                                }
                    }
                } else {
                    firebaseUser?.uid.let {
                        FirebaseDatabase.getInstance().reference
                                .child("Follow").child(it.toString())
                                .child("Following").child(user.getUID())
                                .removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        firebaseUser?.uid.let {
                                            FirebaseDatabase.getInstance().reference
                                                    .child("Follow").child(user.getUID())
                                                    .child("Followers").child(it.toString())
                                                    .removeValue().addOnCompleteListener {
                                                        if (task.isSuccessful) {
                                                            val notMap = HashMap<String,Any>()
                                                            notMap["status"] = "youFalse"
                                                            notMap["otherUser"] = user.getUserName()
                                                            notMap["uploadTime"] = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().time).toString()
                                                            notMap["userId"] = user.getUID()
                                                            FirebaseDatabase.getInstance().reference
                                                                    .child("Notifications")
                                                                    .child(firebaseUser!!.uid)
                                                                    .child(getRandomString(5)).setValue(notMap)

                                                            val notMap2 = HashMap<String,Any>()
                                                            notMap2["status"] = "otherFalse"
                                                            notMap2["otherUser"] = user.getUserName()
                                                            notMap2["uploadTime"] = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().time).toString()
                                                            notMap2["userId"] = user.getUID()
                                                            FirebaseDatabase.getInstance().reference
                                                                    .child("Notifications")
                                                                    .child(user.getUID())
                                                                    .child(getRandomString(5)).setValue(notMap2)
                                                        }
                                                    }
                                        }
                                    }
                                }
                    }
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return mUser.size
    }

    class ViewHolder(@NonNull itemView : View ) : RecyclerView.ViewHolder(itemView){
        var usernameTextView : TextView = itemView.findViewById(R.id.user_name_search)
        var fullnameTextView : TextView = itemView.findViewById(R.id.user_full_name_search)
        var profileImageSearchView : ImageView = itemView.findViewById(R.id.user_profile_image_search)
        var followButton : Button = itemView.findViewById(R.id.following_btn)


    }

    private fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }.trimEnd()

    private fun checkFollowingStatus(uid: String, followButton: Button) {
        val followingRef = firebaseUser?.uid.let {
            FirebaseDatabase.getInstance().reference
                    .child("Follow").child(it.toString())
                    .child("Following")
        }

        followingRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.child(uid).exists()){
                    followButton.text = "Following"
                }
                else{
                    followButton.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
    }

}