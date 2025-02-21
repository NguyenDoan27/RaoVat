package com.example.raovat_app.controllers

import com.example.raovat_app.classes.Response
import com.example.raovat_app.classes.User
import com.example.raovat_app.models.AccountModel
import com.example.raovat_app.models.AccountResult
import com.example.raovat_app.interfaces.ResponseCallback
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AccountController (private val accountModel: AccountModel,private val callback: ResponseCallback) {
     suspend fun signIn(phone: String, password: String) {
         when (val loginResult = accountModel.signIn(phone, password)) {
            is AccountResult.Success -> {
                callback.onSuccess(Response.Success(loginResult.value))
            }
            is AccountResult.Failure -> {
                callback.onError(loginResult.error)
            }

         }
    }

    suspend fun signUp(phone: String, password: String){
        when (val loginResult = accountModel.signUp(phone, password)) {
            is AccountResult.Success -> {
                callback.onSuccess(Response.Success(loginResult.value))
            }
            is AccountResult.Failure -> {
                callback.onError(loginResult.error)
            }
        }
    }

     suspend fun getUser(){
         when(val response = accountModel.getUser()){
            is AccountResult.Success -> {
                callback.onSuccess(Response.Success(response.value))
            }
            is AccountResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }

    suspend fun getAddressDefault(){
        when(val response = accountModel.getAddressDefault()){
            is AccountResult.Success -> {
                callback.onSuccess(Response.SuccessWithExtra(response.value, null))
            }
            is AccountResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }

    suspend fun getAddressList(){
        when (val response = accountModel.getAddressList()){
            is AccountResult.Success -> {
                callback.onSuccess(Response.SuccessWithExtra(response.value, null))
            }
            is AccountResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }

    suspend fun updateAccount(user: RequestBody, image: MultipartBody.Part?){
        when(val response = accountModel.updateAccount(user, image)){
            is AccountResult.Success -> {
                callback.onSuccess(Response.SuccessWith3Params(response.value, null, null))
            }
            is AccountResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }

}