package com.example.instaclone.Model

import java.text.SimpleDateFormat

class NotificationC() {
    private var otherUser : String = ""
    private var status : String = ""
    private var uploadTime : String = ""
    private var userId : String = ""

    constructor(otherUser : String, status: String, uploadTime : String,userId : String) : this(){
        this.otherUser = otherUser
        this.status = status
        this.uploadTime = uploadTime
        this.userId = userId
    }

    fun getOtherUser() : String {
        return otherUser
    }

    fun getStatus() : String {
        return status
    }

    fun getUploadTime() : String {
        return uploadTime
    }

    fun getUserId() : String {
        return userId
    }

}