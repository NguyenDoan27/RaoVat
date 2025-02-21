package com.example.raovat_app.controllers

import com.example.raovat_app.classes.Cart
import com.example.raovat_app.classes.Response
import com.example.raovat_app.models.CartModel
import com.example.raovat_app.models.CartResult
import com.example.raovat_app.models.CartResult1
import com.example.raovat_app.interfaces.ICartCallback


class CartController(private val cartModel: CartModel, private val callback: ICartCallback) {
    suspend fun addToCart(id: Int, cart: Cart) {
        when(val response = cartModel.addToCart(id, cart)){
            is CartResult.Success -> {
                callback.onSuccess(Response.Success(response.value))
            }
            is CartResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }

    suspend fun getCart(){
        when(val response = cartModel.getCart()) {
            is CartResult1.Success -> {
                callback.onSuccess(Response.SuccessWithExtra(response.value, response.value1))
            }

            is CartResult1.Failure -> {
                callback.onError(response.error)
            }
        }
    }

    suspend fun order(ids: ArrayList<Int>){
        when(val response = cartModel.order(ids)){
            is CartResult.Success -> {
                callback.onSuccess(Response.SuccessWith3Params(response.value, null, null))
            }
            is CartResult.Failure -> {
                callback.onError(response.error)
            }            }
    }

    suspend fun updateCart( cart: Cart){
        when(val response = cartModel.updateCart(cart)) {
            is CartResult.Success -> null

            is CartResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }

    suspend fun removeCart(id: Int){
        when(val response = cartModel.removeCart(id)) {
            is CartResult.Success -> {
                callback.onSuccess(Response.SuccessWith3Params(response.value, null, null))
            }
            is CartResult.Failure -> {
                callback.onError(response.error)
            }

        }
    }
}