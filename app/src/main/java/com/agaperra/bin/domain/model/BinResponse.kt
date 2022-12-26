package com.agaperra.bin.domain.model

import com.google.gson.annotations.SerializedName


data class BinResponse(

    @SerializedName("scheme") var scheme: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("brand") var brand: String? = null,
    @SerializedName("country") var country: Country? = Country(),
    @SerializedName("bank") var bank: Bank? = Bank()

)