package com.joshuatabakhoff.caddik

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.joshuatabakhoff.caddik.item.IngredientItem
import com.joshuatabakhoff.caddik.model.Ingredient
import com.joshuatabakhoff.caddik.model.Product
import com.joshuatabakhoff.caddik.network.OPFService
import com.joshuatabakhoff.caddik.model.ProductResult
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_product_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var opfService: OPFService
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        Realm.init(this)

        val barcode = intent.getStringExtra("barcode")

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("Chargement du produit..") // TODO: I18n

        val linearLayoutManager = LinearLayoutManager(this)
        productInfoList.layoutManager = linearLayoutManager

        realm = Realm.getDefaultInstance()

        if(!fetchFromLocal(barcode)){
            fetchProduct(barcode)
        }
    }

    private fun fetchFromLocal(barcode: String): Boolean {
        val product = realm
                .where(Product::class.java)
                .equalTo("code", barcode)
                .findFirst()

        if(product !== null){
            Log.d("CADDIK_STORAGE","Found " + barcode)
            renderProduct(product)
            return true
        }

        return false
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

                if((response.body()?.status ?: 0) != 1){
                    if(!withOBF){
                        Log.d("CADDIK_NETWORK", "Received no product, trying with OpenBeautyFacts")
                        fetchProduct(intent.getStringExtra("barcode"), true)
                        return
                    }else{
                        Log.d("CADDIK_NETWORK", "Received no product, after 2 databases queries")
                        // TODO: Display not found error
                        return
                    }
                }

                val product = response.body()?.product

                renderProduct(product)

                // Save to local database
                realm.beginTransaction()
                realm.copyToRealm(product)
                realm.commitTransaction()
            }
        })
    }

    private fun renderProduct(product: Product?){
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
