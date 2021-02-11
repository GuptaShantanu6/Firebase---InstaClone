package com.example.instaclone.Model

class Comment() {

    private var userName : String = ""
    private var commentDescription : String = ""

    constructor(userName : String ,commentDescription : String) : this(){
        this.userName = userName
        this.commentDescription = commentDescription
    }

    fun getUserName() : String{
        return userName
    }

    fun getCommentDescription() : String {
        return commentDescription
    }

}