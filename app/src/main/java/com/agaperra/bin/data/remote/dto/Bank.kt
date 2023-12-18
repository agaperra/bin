package com.agaperra.bin.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class Bank (

    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("url"   ) var url   : String? = null,
    @SerializedName("phone" ) var phone : String? = null,
    @SerializedName("city"  ) var city  : String? = null

)