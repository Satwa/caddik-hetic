package com.joshuatabakhoff.caddik

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.joshuatabakhoff.caddik.item.IngredientItem
import com.joshuatabakhoff.caddik.model.Ingredient
import com.joshuatabakhoff.caddik.network.OPFService
import com.joshuatabakhoff.caddik.model.ProductResult
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_product_details.*
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
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("Chargement du produit..") // TODO: I18n

        val linearLayoutManager = LinearLayoutManager(this)
        productInfoList.layoutManager = linearLayoutManager
    }


    private fun fetchProduct(barcode: String, withOBF: Boolean = false) {
        var retrofit: Retrofit
        if(withOBF){
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.openbeautyfacts.org/api/v0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }else{
            retrofit = Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/api/v0/") // TODO: world || fr according to locale
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        opfService = retrofit.create(OPFService::class.java)

        opfService.getProductByBarcode(barcode).enqueue(object: Callback<ProductResult> {
            override fun onFailure(call: Call<ProductResult>, t: Throwable) {
                Log.d("CADDIK_NETWORK","Error " + t.message)
                // TODO: Display network error
            }

            override fun onResponse(call: Call<ProductResult>, response: Response<ProductResult>) {
                Log.d("CADDIK_NETWORK", "Received " + response.body()?.product ?: "EMPTY")

                if(response.body()?.status ?: 0 != 1 && !withOBF){
                    Log.d("CADDIK_NETWORK", "Received no product, trying with OpenBeautyFacts")
                    fetchProduct(intent.getStringExtra("barcode"), true)
                    return
                }else{
                    Log.d("CADDIK_NETWORK", "Received no product, after 2 databases query")
                    // TODO: Display not found error
                }

                val product = response.body()?.product

                getSupportActionBar()?.setTitle(product?.product_name)

                // Set product image and save it to cache
                Glide
                    .with(this@ProductDetailsActivity)
                    .load(product?.image_url)
                    //.placeholder(*drawable*)
                    .into(findViewById(R.id.productImage))

                // Show ingredients
                val itemAdapter = ItemAdapter<IngredientItem>()
                val ingredients = product?.ingredients ?: listOf(Ingredient()) // fallback to empty
                itemAdapter.add(ingredients.map { IngredientItem(it) })
                val fastAdapter = FastAdapter.with(itemAdapter)
                productInfoList.adapter = fastAdapter
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
