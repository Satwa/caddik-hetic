package com.joshuatabakhoff.caddik.network

import com.joshuatabakhoff.caddik.network.model.ProductResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

// OpenProductFacts (works for both OpenFoodFacts and OpenBeautyFacts, just switch baseUrl)
interface OPFService {
    @Headers("User-Agent: Caddik - Android - Version 1.0")
    @GET("product/{barcode}.json")
    fun getProductByBarcode(@Path("barcode") barcode: String): Call<ProductResult>

}