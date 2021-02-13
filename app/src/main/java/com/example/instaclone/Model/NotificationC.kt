package com.example.instaclone.Model

class NotificationC() {
    private var userName : String = ""
    private var type : String = ""
    private var userId : String = ""
    private var postType : String = ""

    constructor(userName : String, type: String, userId : String) : this(){
        this.userName = userName
        this.type = type
        this.userId = userId
    }

    fun getUserName() : String{
        return userName
    }

    fun getType() : String {
        return type
    }

    fun getUserId() : String {
        return userId
    }

}