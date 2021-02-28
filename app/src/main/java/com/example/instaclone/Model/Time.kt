package com.example.instaclone.Model

class Time() {
    private var time : String = ""

    constructor(time : String) : this(){
        this.time = time
    }

    fun getTime() : String {
        return time
    }
}