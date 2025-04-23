package com.example.brochures.data.datasource.remote

import retrofit2.Response
import retrofit2.http.GET

interface BrochureApi {
    @GET("shelf.json")
    suspend fun getBrochures(): Response<BrochureResponse>
}
