package com.example.raovat_app.classes

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("url")
    private var avatar: String? = null
    @SerializedName("phone")
    private var phone:String= ""
    @SerializedName("name")
    private var name:String = ""
    @SerializedName("password")
    private var password:String = ""

    constructor(phone: String, name: String, password: String){
        this.phone = phone
        this.name = name
        this.password = password
    }

    constructor(phone:String, password: String){
        this.phone = phone
        this.password = password
    }

    constructor(avatar: String, phone: String, name: String, password: String){
        this.avatar = avatar
        this.phone = phone
        this.name = name
        this.password = password

    }
    fun getAvatar():String?{
        return this.avatar
    }

    fun getPhone():String{
        return this.phone
    }

    fun getName():String {
        return this.name
    }

    fun getPassword(): String {
        return this.password
    }
}