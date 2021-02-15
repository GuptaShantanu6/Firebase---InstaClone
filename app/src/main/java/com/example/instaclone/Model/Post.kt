package com.example.instaclone.Model

import android.security.identity.AccessControlProfileId
import java.text.SimpleDateFormat

class Post() {
    private var Description : String = ""
    private var postId : String = ""
    private var publisher : String = ""
    private var uploadTime : String = ""
    private var postType : Int = 0

    constructor(Description : String, postId : String, publisher : String, uploadTime : String, postType : Int) : this(){
        this.Description = Description
        this.postId = postId
        this.publisher = publisher
        this.uploadTime = uploadTime
        this.postType = postType
    }

    fun getDescription() : String {
        return Description
    }

    fun getpostId() : String {
        return postId
    }

    fun getpublisher() : String {
        return publisher
    }

    fun getUploadTime() : String {
        return  uploadTime
    }

    fun getPostType() : Int{
        return postType
    }



}