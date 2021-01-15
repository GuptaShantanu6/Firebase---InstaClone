package com.example.instaclone.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.instaclone.AccountSettingsActivity
import com.example.instaclone.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var profileId : String
//    private lateinit var firebaseUser : FirebaseUser
//    private lateinit var total_followers : TextView
//    private lateinit var total_following : TextView

//    var total_followers : TextView? = view?.findViewById<TextView>(R.id.total_followers)
//    val total_following : TextView? = view?.findViewById<TextView>(R.id.total_following)
    val storage = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        val editButton : Button = view.findViewById<Button>(R.id.edit_account_settings_btn)
        val total_followers : TextView = view.findViewById<TextView>(R.id.total_followers)
        val total_following : TextView = view.findViewById<TextView>(R.id.total_following)

        val profileImage : CircleImageView = view.findViewById<CircleImageView>(R.id.pro_image_profile_frag)

        val pref = context?.getSharedPreferences("PREFS",Context.MODE_PRIVATE)
        if (pref != null){
            this.profileId = pref.getString("profileId","none").toString()
        }

        var firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!

//        editButton.text = "Edit Profile"

//        if (profileId == firebaseUser.uid){
//            editButton.text = "Edit Profile"
//        }
//        else{
//            checkFollowAndFollowing(editButton,firebaseUser)
//        }



        

        val usernamesettingsbutton : ImageView = view.findViewById<ImageView>(R.id.usernamesettingsbutton)
        usernamesettingsbutton.setOnClickListener {
            Toast.makeText(activity,"Settings in Maintenance",Toast.LENGTH_SHORT).show()
        }


        editButton.setOnClickListener {
            //            Toast.makeText(activity,"Under Maintenance",Toast.LENGTH_SHORT).show()
            startActivity(Intent(context,AccountSettingsActivity::class.java))

        }

        getFollowers(firebaseUser,total_followers)
        getFollowing(firebaseUser,total_following)

        val imagesGrid : ImageButton = view.findViewById<ImageButton>(R.id.images_grid_view_button)
        val imagesSave : ImageButton = view.findViewById<ImageButton>(R.id.images_save_button)

//        total_followers = view.findViewById<TextView>(R.id.total_followers)
//        total_following = view.findViewById<TextView>(R.id.total_following)


        val usernameUpdated : TextView = view.findViewById<TextView>(R.id.usernameText)
        val fullnameUpdated : TextView = view.findViewById<TextView>(R.id.full_name_profile_frag)
        val bioUpdated : TextView = view.findViewById<TextView>(R.id.bio_profile_frag)

//        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
//        val firebaseUser = FirebaseDatabase.getInstance().reference.child("Users")

//        firebaseUser.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
////                usernameUpdated.text = snapshot.child(currentUserId).child("username").value as CharSequence?
////                fullnameUpdated.text = snapshot.child(currentUserId).child("fullName").value as CharSequence?
////                var s : CharSequence? = snapshot.child(currentUserId).child("fullName").value as CharSequence?
//
//                val u1 = snapshot.child(currentUserId).child("fullName").value as CharSequence?
//                val u2 : String = u1.toString().capitalizeFirstLetter()
//
//                usernameUpdated.text = snapshot.child(currentUserId).child("username").value as CharSequence?
//                fullnameUpdated.text = u2
//                bioUpdated.text = snapshot.child(currentUserId).child("bio").value as CharSequence?
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })

        changeUserNameAndFullNameAndBio(usernameUpdated,firebaseUser,fullnameUpdated,bioUpdated)
        checkForProfileImage(firebaseUser,profileImage,profileImage)

        return view
    }

    private fun checkForProfileImage(firebaseUser: FirebaseUser, profileImage: CircleImageView, tempImage: ImageView) {
        storage.child("Default Images/").child(firebaseUser.uid).downloadUrl.addOnSuccessListener {
            val x = it.toString()
            Glide.with(this)
                .load(x)
                .into(tempImage)
        }.addOnFailureListener {
            //Do nothing, i.e. leave the default image as it is.
        }
    }

    private fun changeUserNameAndFullNameAndBio(usernameUpdated: TextView, firebaseUser: FirebaseUser, fullnameUpdated: TextView, bioUpdated: TextView) {
        val database = FirebaseDatabase.getInstance().reference.child("Users")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                usernameUpdated.text = snapshot.child(firebaseUser.uid).child("username").value as CharSequence?
                fullnameUpdated.text = snapshot.child(firebaseUser.uid).child("fullName").value.toString().capitalizeFirstLetter()
                bioUpdated.text = snapshot.child(firebaseUser.uid).child("bio").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun checkFollowAndFollowing(editButton: Button, firebaseUser: FirebaseUser) {
        val followingRef = firebaseUser.uid.let {
            FirebaseDatabase.getInstance().reference
                    .child("Follow").child(it.toString())
                    .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(profileId).exists()){
                    editButton.text = "Following"
                }
                else{
                    editButton.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun getFollowers(firebaseUser: FirebaseUser, total_followers: TextView) {
        val database = FirebaseDatabase.getInstance().reference.child("Follow")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(firebaseUser.uid).child("Followers").exists()){
                    val t = snapshot.child(firebaseUser.uid).child("Followers").childrenCount.toString()
                    total_followers.text = t
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun getFollowing(firebaseUser: FirebaseUser, total_following: TextView) {
        val database = FirebaseDatabase.getInstance().reference.child("Follow")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(firebaseUser.uid).child("Following").exists()){
                    val t = snapshot.child(firebaseUser.uid).child("Following").childrenCount.toString()
                    total_following.text = t
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


    fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize() }.trimEnd()
}

