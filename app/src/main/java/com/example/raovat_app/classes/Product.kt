package com.example.raovat_app.classes

import com.google.gson.annotations.SerializedName

class Product {
    @SerializedName("url")
    private var image: String? = null
    @SerializedName("nameProduct")
    private  var name: String? = null
    private var price: Int? = null
    private var material: String? = null
    private var origin: String? = null
    private var trademark : String? = null
    private var description: String? = null
    private var other: List<Combination>? = null
    private var idStore: Int? = null
    private var address: String? = null
    private var idProduct: Int? = null
    private var amount : Int? = null
    private var sales :Int? = null
    private var idCart: Int? = null
    private var nameStore: String? = null

    constructor(name: String, price: Int, amount: Int, material: String, origin: String, trademark: String, description: String, other: List<Combination>, idStore: Int){
        this.name = name
        this.price = price
        this.material = material
        this.origin = origin
        this.amount = amount
        this.trademark = trademark
        this.description = description
        this.other = other
        this.idStore = idStore
    }
    constructor(idProduct: Int,name: String, price: Int, material: String, origin: String, trademark: String, description: String, sales: Int){
        this.idProduct = idProduct
        this.name = name
        this.price = price
        this.material = material
        this.origin = origin
        this.trademark = trademark
        this.description = description
        this.sales = sales
    }
    constructor(name: String, price: Int, material: String, origin: String, trademark: String, description: String){
        this.name = name
        this.price = price
        this.material = material
        this.origin = origin
        this.trademark = trademark
        this.description = description
    }

    constructor(idProduct: Int, name: String, price: Int, amount: Int, image: String){
        this.idProduct = idProduct
        this.name = name
        this.price = price
        this.amount = amount
        this.image = image
    }

    fun getImage(): String?{
        return image
    }

    fun setImage(image: String) {
        this.image = image
    }

    fun getName(): String?{
        return name
    }

    fun setName(name: String){
        this.name = name
    }

    fun getPrice(): Int?{
        return price
    }
    fun setPrice(price: Int){
        this.price = price
    }
    fun getMaterial(): String?{
        return material
    }
    fun setMaterial( material: String){
        this.material = material
    }
    fun getOrigin(): String?{
        return origin
    }
    fun setOrigin( origin: String){
        this.origin = origin
    }
    fun getTrademark(): String?{
        return trademark
    }
    fun setTrademark(trademark : String){
        this.trademark = trademark
    }
    fun getDescription():String?{
        return description
    }
    fun setDescription(description: String){
        this.description = description
    }

    fun getAddress(): String?{
        return address
    }

    fun getIdProduct(): Int?{
        return idProduct
    }

    fun getAmount(): Int?{
        return amount
    }

    fun getSales(): Int?{
        return sales
    }

    fun getIdCart(): Int?{
        return idCart
    }

    fun getNameStore(): String?{
        return nameStore
    }

}