package com.example.raovat_app.models

import android.util.Log
import com.example.raovat_app.classes.Image
import com.example.raovat_app.classes.Product
import com.example.raovat_app.classes.Store
import com.example.raovat_app.others.PreferenceDataStore
import com.example.raovat_app.utils.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

sealed class StoreResult<T,E>{

    data class Success<T,E>(val value: T, val id: String ): StoreResult<T,E>()
    data class Failure<E,T>(val error: E, val description: String): StoreResult<T,E>()
}

sealed class StoreResult2<T, D, E>{
    data class Success<T, D, E>(val data1: T, val data2: D ): StoreResult2<T, D, E>()
    data class Failure<T, D, E>(val error: E): StoreResult2<T, D, E>()
}
class StoreModel {

    suspend fun checkStore(preferenceDataStore: PreferenceDataStore) : StoreResult<Boolean, Boolean >{
        return preferenceDataStore.readToken().firstOrNull().let  {token ->
            withContext(Dispatchers.IO){

                val response = RetrofitClient.getInstance().checkStore(token.toString()).execute()
                if(response.isSuccessful){
                    val responseBody = response.body()?.string() ?: return@withContext StoreResult.Failure(true, "Không xác định  với token")
                    val jsonResponse = JSONObject(responseBody)
                    val id = jsonResponse.getString("id")
                    return@withContext StoreResult.Success(true, id)
                }else{
                    return@withContext StoreResult.Failure(false, "Lỗi gọi dữ liệu từ server")
                }
            }
        }

    }

    suspend fun getStoreInfo(id: String) : StoreResult<Store, Boolean>{
         return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().getInfoStore(id).execute()
                if(response.isSuccessful){
                    val responseBody = response.body()?.string() ?: return@withContext StoreResult.Failure(true, "Không xác định ")
                    val jsonResponse = JSONObject(responseBody)
                    val store = Store(jsonResponse.getString("nameStore"), jsonResponse.getString("phone"), jsonResponse.getString("address"))
                    return@withContext StoreResult.Success(store, id)
                }else{
                    val errorBody = response.errorBody()?.string() ?: return@withContext StoreResult.Failure(false, "Không xác định ")
                    val jsonError = JSONObject(errorBody)
                    val msg = jsonError.getString("msg")
                    return@withContext StoreResult.Failure(false, msg)
                }

            }catch (_:Exception){
                return@withContext StoreResult.Failure(false, "Lỗi gọi dữ liệu từ server")
            }
        }
    }

    suspend fun getStoreConfirm(id: String) : StoreResult<List<Product>, Boolean>{
        return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().getStoreConfirm(id.toInt()).execute()
                if(response.isSuccessful){
                    val responseBody = response.body()?.string() ?: return@withContext StoreResult.Failure(true, "Không xác định ")
                    val jsonResponse = JSONObject(responseBody)
                    val gson = Gson()
                    val type = jsonResponse.getJSONArray("products")

                    if(type.length() != 0){
                        val listTypeP = object : TypeToken<List<Product>>() {}.type
                        val products: List<Product> = gson.fromJson(type.toString(), listTypeP)

                        return@withContext StoreResult.Success(products, id)
                    }else{
                        return@withContext StoreResult.Failure(false, "Không có đơn hàng cần xác nhận")
                    }

                }else{
                    val errorBody = response.errorBody()?.string() ?: return@withContext StoreResult.Failure(false, "Không xác định ")
                    val jsonError = JSONObject(errorBody)
                    val msg = jsonError.getString("msg")
                    return@withContext StoreResult.Failure(false, msg)
                }

            }catch (_:Exception){
                return@withContext StoreResult.Failure(false, "Lỗi gọi dữ liệu từ server")
            }
        }
    }

    suspend fun accessOder(id: Int): StoreResult<Boolean, Boolean> {
        return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().access(id).execute()
                if(response.isSuccessful){
                    val responseBody = response.body()?.string() ?: return@withContext StoreResult.Failure(true, "Không xác định ")
                    val jsonResponse = JSONObject(responseBody)
                    val status = jsonResponse.getString("status")
                    return@withContext StoreResult.Success(true, status)
                }else{
                    val errorBody = response.errorBody()?.string() ?: return@withContext StoreResult.Failure(false, "Không xác định ")
                    val jsonError = JSONObject(errorBody)
                    val msg = jsonError.getString("msg")
                    return@withContext StoreResult.Failure(false, msg)
                }
            }catch (_:Exception){
                return@withContext StoreResult.Failure(false, "Lỗi gọi dữ liệu từ server")
            }
        }
    }

    suspend fun cancelOder(id: Int): StoreResult<Boolean, Boolean> {
        return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().cancel(id).execute()
                if(response.isSuccessful){
                    val responseBody = response.body()?.string() ?: return@withContext StoreResult.Failure(true, "Không xác định ")
                    val jsonResponse = JSONObject(responseBody)
                    val status = jsonResponse.getString("status")
                    return@withContext StoreResult.Success(true, status)
                }else{
                    val errorBody = response.errorBody()?.string() ?: return@withContext StoreResult.Failure(false, "Không xác định ")
                    val jsonError = JSONObject(errorBody)
                    val msg = jsonError.getString("msg")
                    return@withContext StoreResult.Failure(false, msg)
                }
            }catch (_:Exception){
                return@withContext StoreResult.Failure(false, "Lỗi gọi dữ liệu từ server")
            }
        }
    }

    suspend fun getProduct(id: Int): StoreResult<List<Product>, String> {
        return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().getProduct(id).execute()
                if(response.isSuccessful){
                    val responseBody = response.body()?.string()
                    val jsonObject = JSONObject(responseBody!!)
                    val data = jsonObject.getJSONArray("data")
                    val gson = Gson()
                    val listType = object : TypeToken<List<Product>>() {}.type
                    val products: List<Product> = gson.fromJson(data.toString(), listType)
                    return@withContext StoreResult.Success(products, "")

                }else{
                    val errorBody = response.errorBody()?.string()
                    val msg = JSONObject(errorBody!!).getString("msg")
                    return@withContext StoreResult.Failure(msg, "")
                }
            }catch (_:Exception){
               return@withContext StoreResult.Failure("Lỗi không xác định", "")
            }
        }
    }

    suspend fun getInfoProduct(id: Int): StoreResult2<Product, List<Image>, String>{
        return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().getInfoProduct(id).execute()
                if (response.isSuccessful){
                    val responseBody = response.body()?.string()
                    val jsonObject = JSONObject(responseBody!!)
                    val product: Product = jsonObject.getString("product") as Product
                    val data2 = jsonObject.getJSONArray("image")
                    val gson = Gson()
                    val listType = object: TypeToken<List<Image>>() {}.type
                    val images: List<Image> = gson.fromJson(data2.toString(), listType)
                    return@withContext StoreResult2.Success(product, images)
                }else{
                    val errorBody = response.errorBody()?.string()
                    val msg = JSONObject(errorBody!!).getString("msg")
                    return@withContext StoreResult2.Failure(msg)
                }
            }catch (_:Exception){
                return@withContext StoreResult2.Failure("Lỗi không xác định")
            }
        }
    }
}