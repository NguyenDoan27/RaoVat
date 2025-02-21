package com.example.raovat_app.interfaces

import com.example.raovat_app.classes.Response

interface ResponseCallback {
    fun onSuccess(response: Response<*>)
    fun onError(error: String)

}