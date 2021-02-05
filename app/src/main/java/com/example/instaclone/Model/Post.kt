package com.example.instaclone.Model

import android.security.identity.AccessControlProfileId

class Post() {
    private var Description : String = ""
    private var postId : String = ""
    private var publisher : String = ""

    constructor(Description : String, postId : String, publisher : String) : this(){
        this.Description = Description
        this.postId = postId
        this.publisher = publisher
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



}