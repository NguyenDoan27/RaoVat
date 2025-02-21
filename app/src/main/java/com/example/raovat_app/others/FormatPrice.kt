package com.example.raovat_app.others

import java.text.NumberFormat
import java.util.Locale

object FormatPrice {
    fun formatPrice(price: String): String{
        val priceInt = price.toInt()
        val formattedPrice = NumberFormat.getInstance(Locale.US).format(priceInt)
        return "$formattedPrice đ"
    }
}