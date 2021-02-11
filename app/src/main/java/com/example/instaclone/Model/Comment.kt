package com.example.instaclone.Model

class Comment() {

    private var userName : String = ""
    private var commentDescription : String = ""
    private var commentUserId : String = ""

    constructor(userName : String ,commentDescription : String, commentUserId : String) : this(){
        this.userName = userName
        this.commentDescription = commentDescription
        this.commentUserId = commentUserId
    }

    fun getUserName() : String{
        return userName
    }

    fun getCommentDescription() : String {
        return commentDescription
    }

    fun getCommentUserId() : String {
        return commentUserId
    }

}