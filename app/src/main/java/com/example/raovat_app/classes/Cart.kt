package com.example.raovat_app.classes
import android.os.Parcel
import android.os.Parcelable

class Cart() : Parcelable {
    private var idCart: Int? = null
    private var idProduct: Int? = null
    private var amount: Int? = null

    constructor(parcel: Parcel) : this() {
        idCart = parcel.readValue(Int::class.java.classLoader) as? Int
        idProduct = parcel.readValue(Int::class.java.classLoader) as? Int
        amount = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    constructor(amount: Int?): this(){
        this.amount = amount!!
    }

    fun getIdCart(): Int? {
        return idCart
    }

    fun getAmount(): Int? {
        return amount
    }

    fun setAmount(amount: Int) {
        this.amount = amount
    }
    fun getIdProduct(): Int? {
        return idProduct
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(idCart)
        parcel.writeValue(idProduct)
        parcel.writeValue(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }

}