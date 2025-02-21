package com.example.raovat_app.classes

class Image {
    private var url: String? = null

    constructor(url: String) {
        this.url = url
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String) {
        this.url = url
    }
}