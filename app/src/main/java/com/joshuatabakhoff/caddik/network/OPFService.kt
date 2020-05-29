package com.joshuatabakhoff.caddik.network

import com.joshuatabakhoff.caddik.network.model.ProductResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// OpenProductFacts (works for both OpenFoodFacts and OpenBeautyFacts, just switch baseUrl)
interface OPFService {
    @GET("product/{barcode}.json")
    fun getProductByBarcode(@Query("barcode") barcode: String, @Header("User-Agent") ua: String = "Caddik - Android - Version 1.0"): Call<ProductResult>

}