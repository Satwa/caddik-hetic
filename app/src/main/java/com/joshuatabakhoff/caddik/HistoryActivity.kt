package com.joshuatabakhoff.caddik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.joshuatabakhoff.caddik.item.ProductItem
import com.joshuatabakhoff.caddik.model.Product
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity: AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var itemAdapter: ItemAdapter<ProductItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        Realm.init(this)

        supportActionBar?.setTitle(getString(R.string.history))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val linearLayoutManager = LinearLayoutManager(this)
        historyProductList.layoutManager = linearLayoutManager

        realm = Realm.getDefaultInstance()

        getHistoryAndRender()
    }

    private fun getHistoryAndRender(){
        val _products = realm
                        .where(Product::class.java)
                        .sort("created", Sort.DESCENDING)
                        .findAll()

        val products = realm.copyFromRealm(_products) // We need to perform a copy from realm to access them while filtering (thread-related)

        itemAdapter = ItemAdapter()
        itemAdapter.add(products.map { ProductItem(it) })
        val fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.getSelectExtension().apply {
            isSelectable = true
            multiSelect = true
            selectOnLongClick = false
        }
        historyProductList.adapter = fastAdapter

        fastAdapter.onClickListener = { _, _, item, _ ->
            Log.d("CADDIK_CLICK", item.product.code)
            val intent = Intent(this@HistoryActivity, ProductDetailsActivity::class.java).apply {
                putExtra("barcode", item.product.code)
            }
            startActivity(intent)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.topbar, menu)

        menu.findItem(R.id.history_item).isVisible = false // Hide this button as it is useless

        val searchItem = menu.findItem(R.id.search_item).actionView as SearchView

        searchItem.queryHint = getString(R.string.search_placeholder_product)

        searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private fun lookUp(text: String){
                itemAdapter.filter(text)
                itemAdapter.itemFilter.filterPredicate = { item: ProductItem, constraint: CharSequence? ->
                    item.product.product_name.contains(constraint.toString(), ignoreCase = true)
                }
            }
            override fun onQueryTextChange(newText: String): Boolean {
                lookUp(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("CADDIK_SEARCH", "Performing '$query' search")
                lookUp(query)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.history_item -> {
                Log.d("CADDIK_MENU", "Clicked history item")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}