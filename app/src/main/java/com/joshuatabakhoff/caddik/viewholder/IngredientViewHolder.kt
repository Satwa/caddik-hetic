package com.joshuatabakhoff.caddik.viewholder

import android.view.View
import com.joshuatabakhoff.caddik.item.IngredientItem
import com.mikepenz.fastadapter.FastAdapter
import kotlinx.android.synthetic.main.row_ingredient.view.*

class IngredientViewHolder(itemView: View) : FastAdapter.ViewHolder<IngredientItem>(itemView) {
    override fun bindView(item: IngredientItem, payloads: MutableList<Any>) {
        itemView.ingredientNameTextView.text = item.ingredient.text
    }
    override fun unbindView(item: IngredientItem) {
        itemView.ingredientNameTextView.text = null
    }
}