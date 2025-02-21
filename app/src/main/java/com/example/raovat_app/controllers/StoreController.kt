package com.example.raovat_app.controllers

import com.example.raovat_app.classes.Response
import com.example.raovat_app.fragments.AccountFragment
import com.example.raovat_app.models.StoreModel
import com.example.raovat_app.models.StoreResult
import com.example.raovat_app.others.PreferenceDataStore
import com.example.raovat_app.interfaces.ResponseCallback

class StoreController(private val storeModel: StoreModel, private val callback: ResponseCallback) {
    suspend fun checkStore( accountFragment: AccountFragment, preferenceDataStore: PreferenceDataStore){
        when (val response = storeModel.checkStore(preferenceDataStore)){
            is StoreResult.Success -> {
                accountFragment.startStoreActivity(response.id)
            }
            is StoreResult.Failure -> {
                if(response.error){
                    accountFragment.startCreateStoreActivity()
                }else{
                    accountFragment.showMessenger(response.description)
                }
            }
        }
    }

    suspend fun getStoreInfo(id: String){
        when(val response = storeModel.getStoreInfo(id)){
            is StoreResult.Success -> {
                callback.onSuccess(Response.SuccessWithExtra(response.value, response.id))
            }
            is StoreResult.Failure -> {
                callback.onError(response.description)
            }
        }
    }

     suspend fun getStoreConfirm(id: String){
        when(val response = storeModel.getStoreConfirm(id)){
            is StoreResult.Success -> {
                callback.onSuccess(Response.Success(response.value))
            }
            is StoreResult.Failure -> {
                callback.onError(response.description)
            }
        }
    }

    suspend fun accessOder(id: Int){
        when(val response = storeModel.accessOder(id)){
            is StoreResult.Success -> {
                callback.onSuccess(Response.SuccessWithExtra(response.value, "chap nhan"))
            }
            is StoreResult.Failure -> {
                callback.onError(response.description)
            }
        }
    }

    suspend fun cancelOder(id: Int){
        when(val response = storeModel.cancelOder(id)){
            is StoreResult.Success -> {
                callback.onSuccess(Response.SuccessWithExtra(response.value, "tu choi"))
            }
            is StoreResult.Failure -> {
                callback.onError(response.description)
            }
        }
    }

    suspend fun getProduct(id: Int){
        when(val response = storeModel.getProduct(id)){
            is StoreResult.Success -> {
                callback.onSuccess(Response.Success(response.value))
            }

            is StoreResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }

}