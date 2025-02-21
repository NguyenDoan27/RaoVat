package com.example.raovat_app.models

import com.example.raovat_app.classes.Address
import com.example.raovat_app.others.PreferenceDataStore
import com.example.raovat_app.utils.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.json.JSONObject

sealed class AddressResult <T, E> {
      data class Success<T, E>(val value: T) : AddressResult<T, E>()
      data class Failure<T, E>(val error: E) : AddressResult<T, E>()
  }

    class AddressModel(private val preferenceDataStore: PreferenceDataStore) {
        suspend fun updateAddress(id: Int, address: Address): AddressResult<String, String>{
            return withContext(Dispatchers.IO){
                try {
                    val response = RetrofitClient.getInstance().updateAddress( id, address).execute()
                    if (response.isSuccessful){
                        val responseBody = response.body()?.string() ?: return@withContext AddressResult.Failure("Phản hồi rỗng")
                        val jsonObject = JSONObject(responseBody)
                        val msg = jsonObject.getString("msg")
                        AddressResult.Success(msg)
                    }else{
                        val errorBody = response.errorBody()?.string() ?: return@withContext AddressResult.Failure("Phản hồi rỗng")
                        val jsonObject = JSONObject(errorBody)
                        val msg = jsonObject.getString("msg")
                        AddressResult.Failure(msg)
                    }
                }catch (_:Exception){
                    AddressResult.Failure("Lỗi không xác định")
                }

            }
        }

        suspend fun addAddress( address: Address): AddressResult<String, String>{
           return preferenceDataStore.readToken().firstOrNull()?.let {
               token -> withContext(Dispatchers.IO){
                   try {
                       val response = RetrofitClient.getInstance().addAddress(token, address).execute()
                       if (response.isSuccessful){
                           val responseBody = response.body()?.string() ?: return@withContext AddressResult.Failure("Phản hồi rỗng")
                           val jsonObject = JSONObject(responseBody)
                           val msg = jsonObject.getString("msg")
                           AddressResult.Success(msg)
                       }else{
                           val errorBody = response.errorBody()?.string() ?: return@withContext AddressResult.Failure("Phản hồi rống")
                           val jsonObject = JSONObject(errorBody)
                           val msg = jsonObject.getString("msg")
                           AddressResult.Failure(msg)
                       }
                   }catch (e: Exception){
                       AddressResult.Failure("Lỗi không xác định")
                   }
                }
           }?: AddressResult.Failure("Không tìm thấy token")
        }
    }