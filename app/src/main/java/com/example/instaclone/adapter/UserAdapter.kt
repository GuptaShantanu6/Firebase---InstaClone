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
import java.util.*

class UserAdapter (private var mContext : Context, private var mUser : List<User>, private var isFragment : Boolean = false)
    : RecyclerView.Adapter<UserAdapter.ViewHolder>()
{
    private var firebaseUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val storage = FirebaseStorage.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
//        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false)
//        return UserAdapter.ViewHolder(view)

        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = mUser[position]
        holder.usernameTextView.text = user.getUserName()
        holder.fullnameTextView.text = user.getFullName().capitalizeFirstLetter()
//        Picasso.get().load(user.getImage()).placeholder(R.drawable.ic_man).into(holder.profileImageSearchView)
        storage.child("Default Images/").child(user.getUID()).downloadUrl.addOnSuccessListener {
            val x = it.toString()
            Glide.with(mContext)
                    .load(x)
                    .into(holder.profileImageSearchView)
        }.addOnFailureListener {

        }

        checkFollowingStatus(user.getUID(),holder.followButton)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit()
//            pref.putString("profileId",user.getUID())
//            pref.apply()
            pref.apply{
                putString("profileID",user.getUID())
                apply()
            }
            if (user.getUID() != firebaseUser?.uid){
                val context = mContext
                val intent = Intent(context,UserClickFromSearchActivity::class.java)
                context.startActivity(intent)
            }

//            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container,ProfileFragment()).commit()
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

                                                        }
                                                    }
                                        }
                                    }
                                }
                    }
                }
            }
        }

//        val message : String = user.getFullName().capitalizeFirstLetter()
//        Toast.makeText(mContext,"Clicked in $message",Toast.LENGTH_SHORT).show()


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

    fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize() }.trimEnd()

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

}