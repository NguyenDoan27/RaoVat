package com.example.raovat_app.models

import android.util.Log
import com.example.raovat_app.classes.Cart
import com.example.raovat_app.classes.Product
import com.example.raovat_app.others.PreferenceDataStore
import com.example.raovat_app.utils.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

sealed class CartResult<T, E>{
    data class Success<T, E> (val value: T): CartResult<T, E>()
    data class Failure<T, E> (val error: E): CartResult<T, E>()
}

sealed class CartResult1<T, M, E>{
    data class Success<T, M, E> (val value: T, val value1: M): CartResult1<T, M, E>()
    data class Failure<T, M, E> (val error: E): CartResult1<T, M, E>()
}

class CartModel(private val preferenceDataStore: PreferenceDataStore){
    suspend fun addToCart(id: Int, cart: Cart): CartResult<String, String> {
        return preferenceDataStore.readToken().firstOrNull()?.let { token ->

            withContext(Dispatchers.IO){
                try {
                    val response = RetrofitClient.getInstance().addToCart(token, id, cart).execute()
                    if(response.isSuccessful){
                        val responseBody = response.body()!!.string()
                        Log.e("TAG", "addToCart: $responseBody", )
                        val jsonObject = JSONObject(responseBody)
                        val msg = jsonObject.getString("msg")
                        Log.e("TAG", "addToCart: $msg", )
                        CartResult.Success(msg)
                    }else{
                        val responseBody = response.errorBody()!!.string()
                        val jsonObject = JSONObject(responseBody)
                        val msg = jsonObject.getString("msg")
                        CartResult.Failure(msg)
                    }
                }catch (_: Exception){
                    CartResult.Failure("Lỗi không xác định")
                }
            }
        }?: CartResult.Failure("Không tìm thấy token")
    }

    suspend fun getCart() : CartResult1<List<Cart>, List<Product>, String> {
        return preferenceDataStore.readToken().firstOrNull()?.let { token ->
            withContext(Dispatchers.IO){
                try{
                    val response = RetrofitClient.getInstance().getCart(token).execute()
                    if(response.isSuccessful){
                        val responseBody = response.body()!!.string()
                        val jsonObject = JSONObject(responseBody)
                        val cart = jsonObject.getJSONArray("cart")
                        val product = jsonObject.getJSONArray("product")
                        val gson = Gson()
                        val listTypeC = object : TypeToken<List<Cart>>() {}.type
                        val carts: List<Cart> = gson.fromJson(cart.toString(), listTypeC)
                        val listTypeP = object : TypeToken<List<Product>>() {}.type
                        val products: List<Product> = gson.fromJson(product.toString(), listTypeP)
                        CartResult1.Success(carts, products)
                    }else{
                        val responseBody = response.errorBody()!!.string()
                        val jsonObject = JSONObject(responseBody)
                        val msg = jsonObject.getString("msg")
                        CartResult1.Failure(msg)
                    }
                }catch(_:Exception){
                    CartResult1.Failure("Lỗi không xác định")
                }
            }
        }?: CartResult1.Failure("Không tìm thấy token")
    }

    suspend fun order(ids: ArrayList<Int>): CartResult<String, String> {
        return preferenceDataStore.readToken().firstOrNull().let{ token ->
            withContext(Dispatchers.IO){
                try {
                    val response = RetrofitClient.getInstance().order(token!!, ids).execute()
                    if(response.isSuccessful) {
                        val responseBody = response.body()!!.string()
                        val jsonObject = JSONObject(responseBody)
                        val msg = jsonObject.getString("msg")
                        CartResult.Success(msg)
                    }else{
                        val responseBody = response.errorBody()!!.string()
                        val jsonObject = JSONObject(responseBody)
                        val msg = jsonObject.getString("msg")
                        CartResult.Failure(msg)
                    }
                }catch (_:Exception){
                    CartResult.Failure("Lỗi không xác định")
                }
            }
        }
    }

    suspend fun updateCart(cart: Cart): CartResult<Boolean, String> {
        return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().updateCart(cart).execute()
                if(response.isSuccessful) {
                    CartResult.Success(true)
                }else{
                    val responseBody = response.errorBody()!!.string()
                    val jsonObject = JSONObject(responseBody)
                    val msg = jsonObject.getString("msg")
                    CartResult.Failure(msg)
                }
            }catch (_:Exception){
                CartResult.Failure("Lỗi không xác định")
            }

        }
    }

    suspend fun removeCart(id: Int): CartResult<Boolean, String> {
        return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().removeCart(id).execute()
                if (response.isSuccessful) {
                    CartResult.Success(true)
                } else {
                    val responseBody = response.errorBody()!!.string()
                    val jsonObject = JSONObject(responseBody)
                    val msg = jsonObject.getString("msg")
                    CartResult.Failure(msg)
                }
            }catch (_:Exception){
                CartResult.Failure("Lỗi không xác định")
            }

        }
    }

}