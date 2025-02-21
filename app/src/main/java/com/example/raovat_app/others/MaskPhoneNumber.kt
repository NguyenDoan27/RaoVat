package com.example.raovat_app.others

object MaskPhoneNumber {
    fun mask(phone: String) : String{
        if (phone.length <= 4) {
            return phone
        }

        val maskLength = phone.length - 6
        val maskedPart = "*".repeat(maskLength)
        val lastDigits = phone.takeLast(3)
        val firstDigits = phone.take(3)

        return firstDigits + maskedPart + lastDigits
    }
}