package com.joshuatabakhoff.caddik.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.joshuatabakhoff.caddik.item.ProductItem
import com.mikepenz.fastadapter.FastAdapter
import kotlinx.android.synthetic.main.row_product.view.*

class ProductViewHolder(itemView: View) : FastAdapter.ViewHolder<ProductItem>(itemView) {
    override fun bindView(item: ProductItem, payloads: MutableList<Any>) {
        itemView.productNameTextView.text = item.product.product_name
        itemView.productBrandTextView.text = item.product.brands
        Glide
            .with(this.itemView)
            .load(item.product.image_url)
            //.placeholder(*drawable*)
            .into(itemView.rowProductImage)
    }

    override fun unbindView(item: ProductItem) {
        itemView.productNameTextView.text = null
        itemView.productBrandTextView.text = null
        itemView.rowProductImage.setImageResource(android.R.color.transparent)

    }
}