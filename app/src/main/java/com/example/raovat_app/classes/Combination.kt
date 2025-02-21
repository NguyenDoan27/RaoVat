package com.example.raovat_app.classes

import java.io.Serializable

class Combination: Serializable {
    private var name1: String = ""
    private var name2: String = ""
    private var combination1: String = ""
    private var combination2: String = ""
    private var amount: Int = 0
    private var price: Int = 0

    constructor(name: String, combination: String, amount: Int, price: Int){
        this.name1 = name
        this.combination1 = combination
        this.amount = amount
        this.price = price
    }

    constructor(name: String, combination1: String, name2: String, combination2: String, amount: Int, price: Int){
        this.name1 = name
        this.name2 = name2
        this.combination1 = combination1
        this.combination2 = combination2
        this.amount = amount
        this.price = price
    }
    fun getName1(): String {
        return  name1
    }
    fun setName1(name: String){
        this.name1 = name
    }

    fun getName2(): String {
        return name2
    }

    fun setName2(name: String){
        this.name2 = name
    }

    fun  getCombination1(): String{
        return combination1
    }

    fun setCombination1(combination: String){
        this.combination1 = combination
    }

    fun getCombination2(): String {
        return  combination2
    }

    fun setCombination2( combination: String){
        this.combination2 = combination
    }

    fun getAmount(): Int {
        return amount
    }

    fun setAmount(amount: Int){
        this.amount = amount
    }

    fun getPrice(): Int {
        return price
    }

    fun setPrice(price: Int){
        this.price = price
    }
}