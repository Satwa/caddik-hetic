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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        Realm.init(this)

        supportActionBar?.setTitle("Historique") // TODO: I18n
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val linearLayoutManager = LinearLayoutManager(this)
        historyProductList.layoutManager = linearLayoutManager

        realm = Realm.getDefaultInstance()

        getHistoryAndRender()
    }

    private fun getHistoryAndRender(){
        val products = realm
                        .where(Product::class.java)
                        .sort("created", Sort.DESCENDING)
                        .findAll()

        val itemAdapter = ItemAdapter<ProductItem>()
        itemAdapter.add(products.map { ProductItem(it) })
        val fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.getSelectExtension().apply {
            isSelectable = true
            multiSelect = true
            selectOnLongClick = false
        }
        historyProductList.adapter = fastAdapter

        fastAdapter.onClickListener = { view, adapter, item, position ->
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

        searchItem.queryHint = "Entrez le nom d'un produit" // TODO: I18n

        searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                // Should be done live here
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("CADDIK_SEARCH", "Performing '$query' search")
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