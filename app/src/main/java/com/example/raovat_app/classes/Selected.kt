package com.example.raovat_app.classes

import java.io.Serializable

class Selected(name: String, value1: String, value2: String, value3: String, value4: String) : Serializable {
    private var name: String? = name
    private var value1:String? = value1
    private var value2: String? = value2
    private var value3: String? = value3
    private var value4: String? = value4

    fun getName(): String?{
        return name
    }
    fun setName(name: String){
        this.name = name
    }
    fun getValue1(): String? {
        return value1
    }
    fun setValue1(value: String){
        this.value1 = value
    }
    fun getValue2(): String?{
        return value2
    }
    fun setValue2(value: String){
        this.value2 = value
    }
    fun getValue3(): String?{
        return value3
    }
    fun setValue3(value: String){
        this.value3 = value
    }
    fun getValue4(): String?{
        return value4
    }
    fun setValue4(value: String){
        this.value4 = value
    }
}