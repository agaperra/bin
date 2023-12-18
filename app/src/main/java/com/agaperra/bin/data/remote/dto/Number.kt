package com.agaperra.bin.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class Number (

    @SerializedName("length" ) var length : Int?     = null,
    @SerializedName("luhn"   ) var luhn   : Boolean? = null

)