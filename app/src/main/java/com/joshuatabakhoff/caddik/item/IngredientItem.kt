package com.joshuatabakhoff.caddik.item

import android.view.View
import com.joshuatabakhoff.caddik.R
import com.joshuatabakhoff.caddik.model.Ingredient
import com.joshuatabakhoff.caddik.viewholder.IngredientViewHolder
import com.mikepenz.fastadapter.items.AbstractItem

class IngredientItem(val ingredient: Ingredient): AbstractItem<IngredientViewHolder>() {
    override val layoutRes: Int
        get() = R.layout.row_ingredient
    override val type: Int
        get() = R.id.ingredientNameTextView
    override fun getViewHolder(v: View): IngredientViewHolder {
        return IngredientViewHolder(v)
    }
}
