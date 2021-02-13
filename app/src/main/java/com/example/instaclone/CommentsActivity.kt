package com.example.instaclone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.instaclone.Model.Comment
import com.example.instaclone.adapter.CommentAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentsActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var commentAdapter : CommentAdapter? = null
    private var mComment : MutableList<Comment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val closeBtn : ImageView = findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener {
            finish()
        }

        val currentUser = FirebaseAuth.getInstance().currentUser

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComment)
        recyclerView?.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(baseContext)
        recyclerView!!.layoutManager = linearLayoutManager

        mComment = ArrayList()
        commentAdapter = baseContext?.let { CommentAdapter(it,false, mComment as ArrayList<Comment>) }
        recyclerView?.adapter = commentAdapter

        val submitBtn : FloatingActionButton = findViewById(R.id.submitComment)
//        val comment : EditText = findViewById(R.id.enterComment)
        val typedComment = findViewById<TextView>(R.id.enterComment)

        submitBtn.isClickable = true

        val commentAnim : LottieAnimationView = findViewById(R.id.commentSubmitAnim)
        commentAnim.setAnimation("629-empty-box.json")

        val whichPost = baseContext.getSharedPreferences("whichIDs",Context.MODE_PRIVATE)
        val postID = whichPost.getString("postID","none").toString()
        val postPublisher = whichPost.getString("postPublisher","none").toString()

        Log.d("postId",postID)

        initiator2(postID, commentAnim)

        submitBtn.setOnClickListener {
            val tempId = getRandomString(5)
            if (typedComment.text.toString().isBlank()){
                Toast.makeText(this,"Please enter something",Toast.LENGTH_SHORT).show()
            }
            else {
//                commentAnim.playAnimation()
//                commentAnim.loop(true)
                submitBtn.isClickable = false
                val db = FirebaseDatabase.getInstance().reference
//                db.child("Comments").child(postID).child(currentUser!!.uid).child("commentDescription").child()

//                db.child("Comments").child(postID).child(currentUser!!.uid).child(tempId).child("commentDescription").setValue(typedComment.text.toString())
//                db.child("Comments").child(postID).child(currentUser.uid).child(tempId).child("commentUserId").setValue(currentUser.uid)

                db.child("Comments").child(postID).child(tempId).child("commentDescription").setValue(typedComment.text.toString())
                db.child("Comments").child(postID).child(tempId).child("commentUserId").setValue(currentUser!!.uid)
                var x = ""
                db.child("Users").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        x = snapshot.child(currentUser.uid).child("username").value.toString()
                        db.child("Comments").child(postID).child(tempId).child("userName").setValue(x).addOnCompleteListener {
//                            initiator2(postID, commentAnim,submitBtn)
//                            commentAnim.loop(false)
                            submitBtn.isClickable = true
                            Toast.makeText(this@CommentsActivity,"Comment posted, please exit to refresh",Toast.LENGTH_SHORT).show()
                            initiator2(postID,commentAnim)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }

        }


    }

    private fun initiator2(postID: String, commentAnim: LottieAnimationView) {
        val db = FirebaseDatabase.getInstance().reference
        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.child("Comments").exists()){
//                    for (ss in snapshot.child("Comments").children){
//                        for (cccc in ss.children){
//                            for (xxxx in cccc.children){
//                                val c = xxxx.getValue(Comment::class.java)
//                                if (c != null){
//                                    mComment?.add(c)
//                                }
//                            }
//                        }
//                    }
//                }
//                if (snapshot.child("Comments").child(postID).exists()){
//                    for (ss in snapshot.child("Comments").child(postID).children)
//                        for (xx in ss.children)
//                }
//                commentAdapter?.notifyDataSetChanged()
                if (snapshot.child("Comments").child(postID).exists()){
                    for (xx in snapshot.child("Comments").child(postID).children){
                        val c = xx.getValue(Comment::class.java)
                        if (c != null){
                            mComment?.add(c)
                        }
                    }
                }
                if (mComment?.isEmpty() == true){
                    commentAnim.playAnimation()
                    commentAnim.loop(true)
                }
                else {
                    commentAnim.visibility = View.GONE
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