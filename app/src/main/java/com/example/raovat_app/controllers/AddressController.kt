package com.example.raovat_app.controllers

import com.example.raovat_app.classes.Address
import com.example.raovat_app.classes.Response
import com.example.raovat_app.interfaces.ResponseCallback
import com.example.raovat_app.models.AddressModel
import com.example.raovat_app.models.AddressResult

class AddressController(private val addressModel: AddressModel, private val callback: ResponseCallback) {
    suspend fun updateAddress (id: Int, address: Address){
        when(val response = addressModel.updateAddress(id, address)){
            is AddressResult.Success -> {
                callback.onSuccess(Response.SuccessWith3Params(response.value, null, null))
            }
            is AddressResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }

    suspend fun addAddress( address: Address){
        when(val response = addressModel.addAddress(address)){
            is AddressResult.Success -> {
                callback.onSuccess(Response.SuccessWith3Params(response.value, null, null))
            }
            is AddressResult.Failure -> {
                callback.onError(response.error)
            }
        }
    }
}