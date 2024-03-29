package com.agaperra.bin.data.api

import com.agaperra.bin.data.remote.dto.CarsAnswerResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BinApi {

    @GET("{number}")
    suspend fun getCardInfoByNumber(
        @Path("number", encoded = true) number: String?,
    ): CarsAnswerResponse

}