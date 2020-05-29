package com.joshuatabakhoff.caddik

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.joshuatabakhoff.caddik.network.OPFService
import com.joshuatabakhoff.caddik.network.model.ProductResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var opfService: OPFService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        val barcode = intent.getStringExtra("barcode")

        fetchProduct(barcode)
    }


    private fun fetchProduct(barcode: String) {
        val d = Log.d("CADDIK_CAMERA", "Received a barcode: " + barcode)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/api/v0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        opfService = retrofit.create(OPFService::class.java)

        opfService.getProductByBarcode(barcode).enqueue(object: Callback<ProductResult> {
            override fun onFailure(call: Call<ProductResult>, t: Throwable) {
                Log.d("CADDIK_NETWORK","Error " + t.message)
            }

            override fun onResponse(call: Call<ProductResult>, response: Response<ProductResult>) {
                Log.d("CADDIK_NETWORK", "Receive " + response.body()?.product ?: "EMPTY")
            }
        })
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            //R.id.scanButton -> {
            //    if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //        openCamera()
            //    }else{
            //        // Ask for the permission.
            //        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            //    }
            //}
        }
    }

}
