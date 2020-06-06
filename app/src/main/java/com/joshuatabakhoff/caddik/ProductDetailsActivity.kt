package com.joshuatabakhoff.caddik

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
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

        val barcode = intent.getStringExtra("barcode") as String

        // Change actionBar title and show back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.product_loading)

        // setup product info recyclerview
        val linearLayoutManager = LinearLayoutManager(this)
        productInfoList.layoutManager = linearLayoutManager

        realm = Realm.getDefaultInstance()

        // find locally or perform api request
        if(!fetchFromLocal(barcode)){
            fetchProduct(barcode)
        }
    }


    private fun showDialog(title: String, description: String) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        builder.setTitle(title)

        // Display a message on alert dialog
        builder.setMessage(description)

        // Set a positive button and its click listener on alert dialog
        builder.setNeutralButton("OK"){dialog, _ ->
            dialog.dismiss()
            finish()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    private fun fetchFromLocal(barcode: String): Boolean {
        val product = realm
                .where(Product::class.java)
                .equalTo("code", barcode)
                .findFirst()

        if(product !== null){
            Log.d("CADDIK_STORAGE","Found " + barcode)
            renderProduct(product)

            // Update created to be accurate in history
            realm.beginTransaction()
            product.created = (System.currentTimeMillis() / 1000).toString()
            realm.commitTransaction()

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

        // Perform HTTP request
        opfService.getProductByBarcode(barcode).enqueue(object: Callback<ProductResult> {
            override fun onFailure(call: Call<ProductResult>, t: Throwable) {
                Log.d("CADDIK_NETWORK","Error " + t.message)
                showDialog(getString(R.string.error), getString(R.string.error_network))
            }

            override fun onResponse(call: Call<ProductResult>, response: Response<ProductResult>) {
                Log.d("CADDIK_NETWORK", "Received " + response.body()?.product)

                if(response.body()?.status != 1 || (response.body()?.product?.product_name?.length ?: 0) < 2){
                    // Product not found
                    if(!withOBF){
                        // We received no product with OpenFoodFacts, we try with OpenBeautyFacts
                        fetchProduct(intent.getStringExtra("barcode") as String, true)
                    }else{
                        // We failed to find a product, we display an error
                        showDialog(getString(R.string.error), getString(R.string.error_not_found))
                    }
                }else{
                    // Product found
                    val product = response.body()?.product as Product

                    renderProduct(product)

                    // Save to local database
                    realm.beginTransaction()
                    realm.copyToRealmOrUpdate(product)
                    realm.commitTransaction()
                }
            }
        })
    }

    private fun renderProduct(product: Product?){
        supportActionBar?.title = product?.product_name

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

        progressBar.visibility = View.INVISIBLE
    }

    // Back button on actionBar handler
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
