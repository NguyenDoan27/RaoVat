package com.example.raovat_app.models

import android.util.Log
import com.example.raovat_app.classes.Cart
import com.example.raovat_app.classes.Image
import com.example.raovat_app.classes.Product
import com.example.raovat_app.classes.Store
import com.example.raovat_app.utils.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

sealed class ProductResult<T, E> {
    data class Success<T, E>(val value: T) : ProductResult<T, E>()
    data class Failure<T, E>(val error: E) : ProductResult<T, E>()
}

sealed class ProductResult2<T ,D ,M ,S , E> {
    data class Sucess<T,D, M, S, E>(val value: T, val value1: D, val value2: M, val list : S): ProductResult2<T ,D ,M ,S, E>()
    data class Failure<T ,D ,M, S , E>(val error: E) : ProductResult2<T ,D ,M , S, E>()
}
class ProductModels {
    //todo add product
    suspend fun add(value: RequestBody, images: List<MultipartBody.Part>): ProductResult<Boolean, String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getInstance().addProduct(value, images).execute() // Gọi API
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    if (responseBody != null) {
                        val responseJSON = JSONObject(responseBody)
                        val msg: String = responseJSON.getString("msg")
                        Log.e("TAG", "onResponse: $msg")
                    }
                    ProductResult.Success(true) // Trả về kết quả thành công
                } else {
                    val errorBody = response.errorBody()?.string() ?: return@withContext ProductResult.Failure("Lỗi không xác định") // lỗi không xác định
                    val errorJson = JSONObject(errorBody)
                    val errorMessage = errorJson.getString("msg")
                    ProductResult.Failure(errorMessage) // Trả về thông báo lỗi
                }
            } catch (e: Exception) {

                ProductResult.Failure("Lỗi gọi dữ liệu từ server") // Trả về lỗi không gọi được dữ liệu
            }
        }
    }
    //todo get all product
    suspend fun getAllProduct(): ProductResult<List<Product>, String> {
          return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().getAllProduct().execute()  // gọi api
                if (response.isSuccessful){
                    val responseBody = response.body()?.string()
                    val responseArray = JSONArray(responseBody)
                    val gson = Gson()
                    val listType = object : TypeToken<List<Product>>() {}.type
                    val products: List<Product> = gson.fromJson(responseArray.toString(), listType)
                    return@withContext ProductResult.Success(products) // trả về thành công
                }else{
                    val errorBody = response.errorBody()?.string() ?:  return@withContext ProductResult.Failure("Lỗi không xác định") // lỗi không xác định
                    val errorJson = JSONObject(errorBody)
                    val errorMessage = errorJson.getString("msg")
                    return@withContext ProductResult.Failure(errorMessage) // trả về lỗi
                }
            }catch (_:Exception){

                return@withContext ProductResult.Failure("Lỗi gọi dữ liệu từ server") // trả về lỗi không gọi được dữ liệu
            }

        }
    }
    // todo get info of product, store, image by id product
    suspend fun getProductById(id: Int) : ProductResult2<Product, Store, List<Image>,List<Product>, String>{
        return withContext(Dispatchers.IO){
            try {

                val response = RetrofitClient.getInstance().getProductsById(id).execute()

                if(response.isSuccessful){
                    val responseBody = response.body()?.string() ?: return@withContext ProductResult2.Failure("Lỗi không xác định")
                    val responseJSON = JSONObject(responseBody)

                    val productData = responseJSON.getJSONArray("product")
                    val storeData = responseJSON.getJSONArray("store")
                    val imageData = responseJSON.getJSONArray("images")
                    val list = responseJSON.getJSONArray("list")

                    val productObject = productData.getJSONObject(0)
                    val product = Product(
                        productObject.getString("idProduct").toInt(),
                        productObject.getString("nameProduct"),
                        productObject.getInt("price"),
                        productObject.getString("material"),
                        productObject.getString("origin"),
                        productObject.getString("trademark"),
                        productObject.getString("description"),
                        productObject.getString("sales").toInt()
                    )

                    val storeObject = storeData.getJSONObject(0)
                    val store = Store(storeObject.getString("idStore").toInt(), storeObject.getString("nameStore"), storeObject.getString("address"))

                    val gson = Gson()
                    val listType = object : TypeToken<List<Image>>() {}.type
                    val images: List<Image> = gson.fromJson(imageData.toString(), listType)

                    val type = object : TypeToken<List<Product>>() {}.type
                    val productList: List<Product> = gson.fromJson(list.toString(), type)

                    return@withContext ProductResult2.Sucess(product, store, images, productList)
                }else{
                    val errorBody = response.errorBody()?.string() ?: return@withContext ProductResult2.Failure("Lỗi không xác định") // lỗi không xác định
                    val errorJson = JSONObject(errorBody)
                    val errorMessage = errorJson.getString("msg")
                    return@withContext ProductResult2.Failure(errorMessage)
                }

            }catch (_:Exception){
                return@withContext ProductResult2.Failure("Lỗi gọi dữ liệu từ server")
            }
        }
    }

    suspend fun getOrders(ids: ArrayList<Int>): ProductResult<List<Product>, String>{
        return withContext(Dispatchers.IO){
            try {
                val response = RetrofitClient.getInstance().getOrder(ids).execute()
                if(response.isSuccessful){
                    val responseBody = response.body()!!.string()
                    val jsonObject = JSONObject(responseBody)
                    val data = jsonObject.getJSONArray("data")
                    val gson = Gson()
                    val listTypeC = object : TypeToken<ArrayList<Product>>() {}.type
                    val products: ArrayList<Product> = gson.fromJson(data.toString(), listTypeC)
                    ProductResult.Success(products)
                }else{
                    val responseBody = response.errorBody()!!.string()
                    val jsonObject = JSONObject(responseBody)
                    val msg = jsonObject.getString("msg")
                    ProductResult.Failure(msg)
                }
            }catch (e:Exception){
                ProductResult.Failure("Lỗi không xác định ")

            }
        }
    }

}