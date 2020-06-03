package com.joshuatabakhoff.caddik.item

import android.view.View
import com.joshuatabakhoff.caddik.R
import com.joshuatabakhoff.caddik.model.Product
import com.joshuatabakhoff.caddik.viewholder.ProductViewHolder
import com.mikepenz.fastadapter.items.AbstractItem

class ProductItem(val product: Product): AbstractItem<ProductViewHolder>() {
    override val layoutRes: Int
        get() = R.layout.row_product
    override val type: Int
        get() = R.id.productNameTextView
    override fun getViewHolder(v: View): ProductViewHolder {
        return ProductViewHolder(v)
    }
}
