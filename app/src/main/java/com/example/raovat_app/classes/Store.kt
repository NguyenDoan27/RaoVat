package com.example.raovat_app.classes

import com.google.gson.annotations.SerializedName

class Store {
    @SerializedName("idStore")
    private var id : Int? = null
    @SerializedName("nameStore")
    private var name: String? = null
    private var phone: String? = null
    private var address: String? = null
    private var description: String? = null

    constructor(name: String, phone:String, address: String){
        this.name = name
        this.phone = phone
        this.address = address

    }

    constructor(id: Int, name: String, address: String){
        this.id = id
        this.name = name
        this.address = address

    }

    fun getId(): Int?{
        return id
    }

    fun setId(id: Int){
        this.id = id
    }

    fun getName() : String? {
        return name
    }

    fun setName(name: String){
        this.name = name
    }

    fun getPhone() : String? {
        return phone
    }

    fun setPhone(phone : String){
        this.phone = phone
    }

    fun getAddress(): String? {
        return address
    }

    fun  setAddress( address: String){
        this.address = address
    }

    fun setDescription(description: String){
        this.description = description
    }

    fun getDescription(): String? {
        return description
    }
}