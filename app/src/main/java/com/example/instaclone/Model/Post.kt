package com.example.instaclone.Model

import android.security.identity.AccessControlProfileId
import java.text.SimpleDateFormat

class Post() {
    private var Description : String = ""
    private var postId : String = ""
    private var publisher : String = ""
    private var uploadTime : String = ""

    constructor(Description : String, postId : String, publisher : String, uploadTime : String) : this(){
        this.Description = Description
        this.postId = postId
        this.publisher = publisher
        this.uploadTime = uploadTime
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



}