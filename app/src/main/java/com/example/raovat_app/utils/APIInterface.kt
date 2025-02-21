package com.example.raovat_app.utils

import com.example.raovat_app.classes.Address
import com.example.raovat_app.classes.Cart
import com.example.raovat_app.classes.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface APIInterface {
    //register
    @POST("register")
    fun regisUser(@Body user: User): Call<ResponseBody>
    //login
    @POST("login")
    fun login(@Body user: User): Call<ResponseBody>
    //check store exist or not
    @Headers("Content-Type: application/json")
    @GET("myStore")
    fun checkStore(@Header("Authorization") token: String) : Call<ResponseBody>
    //get profile
    @GET("profile")
    fun getProfile(@Header("Authorization") token: String ): Call<ResponseBody>
    //get info store
    @GET("store/{id}")
    fun getInfoStore(@Path("id") id : String): Call<ResponseBody>
    //post data product and images
    @Multipart
    @POST("product/add")
    fun addProduct(@Part("product") productJson: RequestBody, @Part images: List<MultipartBody.Part>):Call<ResponseBody>
    // get all product
    @GET("product")
    fun getAllProduct(): Call<ResponseBody>
    //get info of product, store, image by id product
    @GET("product/{id}")
    fun getProductsById(@Path("id") id: Int): Call<ResponseBody>

    /**
     * @GET("/product/combination/{id}")
     *     fun getCombinationById(@Path("id") id: Int): Call<ResponseBody>
     */
    @POST("cart/add/{id}")
    fun addToCart(@Header("Authorization") token: String, @Path("id") id: Int, @Body cart: Cart): Call<ResponseBody>
    @POST("cart/update")
    fun updateCart( @Body cart: Cart): Call<ResponseBody>
    @GET("cart")
    fun getCart(@Header("Authorization") token: String): Call<ResponseBody>
    @POST("cart/order")
    fun order(@Header("Authorization") token: String, @Body ids: ArrayList<Int>): Call<ResponseBody>
    @GET("store/confirm/{id}")
    fun getStoreConfirm(@Path("id") id: Int): Call<ResponseBody>
    @POST("store/access/{id}")
    fun access(@Path("id") id: Int): Call<ResponseBody>
    @POST("store/cancel/{id}")
    fun cancel(@Path("id") id: Int): Call<ResponseBody>
    @POST("cart/order/check")
    fun getOrder(@Body ids: ArrayList<Int>): Call<ResponseBody>
    @GET("account/address")
    fun getAddressDefault(@Header("Authorization") token: String): Call<ResponseBody>
    @POST("cart/remove/{id}")
    fun removeCart(@Path("id") id: Int): Call<ResponseBody>
    @GET("account/address/list")
    fun getAddressList(@Header("Authorization") token: String): Call<ResponseBody>
    @Multipart
    @POST("account/update")
    fun updateAccount(@Header("Authorization") token: String, @Part("user") user: RequestBody,@Part image: MultipartBody.Part? ): Call<ResponseBody>
    @POST("address/update/{id}")
    fun updateAddress( @Path("id") id: Int, @Body address: Address): Call<ResponseBody>
    @POST("address/add")
    fun addAddress(@Header("Authorization") token : String, @Body address: Address): Call<ResponseBody>
    @GET("store/products/{id}")
    fun getProduct(@Path("id")id : Int): Call<ResponseBody>
    @GET("store/product/{id}")
    fun getInfoProduct(@Path("id")id: Int): Call<ResponseBody>
}