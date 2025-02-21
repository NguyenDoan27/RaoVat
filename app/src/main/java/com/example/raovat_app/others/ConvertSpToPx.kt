package com.example.raovat_app.others

import android.content.Context
import android.util.TypedValue

object ConvertSpToPx {
    fun convertSpToPx(context: Context, sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
    }
}