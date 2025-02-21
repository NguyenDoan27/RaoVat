package com.example.raovat_app.controllers

import com.example.raovat_app.classes.Response
import com.example.raovat_app.models.ProductModels
import com.example.raovat_app.models.ProductResult
import com.example.raovat_app.models.ProductResult2
import com.example.raovat_app.interfaces.ResponseCallback
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProductController(private val productModel: ProductModels,private val callback: ResponseCallback) {
    suspend fun addProduct(value: RequestBody, images: List<MultipartBody.Part>){
        when (val addResult = productModel.add(value, images)) {
            is ProductResult.Success -> {
                callback.onSuccess(Response.Success("Them thanh cong"))
            }
            is ProductResult.Failure -> {
                callback.onError(addResult.error)
            }
        }
    }

    suspend fun getAllProduct(){
        when(val response = productModel.getAllProduct()){
            is ProductResult.Success -> {
                callback.onSuccess(Response.Success(response.value))
            }
            is ProductResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }

    suspend fun getProductById(id: Int){
        when(val response = productModel.getProductById(id)){
            is ProductResult2.Sucess -> {
                callback.onSuccess(Response.SuccessWith3Params(response.value, response.value1, response.value2))
                callback.onSuccess(Response.SuccessWithExtra(response.list, null))
            }
            is ProductResult2.Failure -> {
                callback.onError(response.error)
            }

        }
    }
    suspend fun getOrdersById(ids: ArrayList<Int>){
        when(val response = productModel.getOrders(ids)){
            is ProductResult.Success -> {
                callback.onSuccess(Response.Success(response.value))
            }
            is ProductResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }
}