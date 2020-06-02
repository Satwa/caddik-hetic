package com.joshuatabakhoff.caddik.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Product: RealmObject() {
    @PrimaryKey
    var code         = ""
    var product_name = ""
    var generic_name = ""
    var image_url    = ""
    var brand_owner  = ""
    var brands       = ""
    var origins: String?      = ""
    var labels: String?       = ""
    var quantity: String?     = ""

    var ingredients: RealmList<Ingredient> = RealmList<Ingredient>()
    // var ingredients = listOf<Ingredient>()

    var ingredients_text: String? = ""
    var ingredients_text_with_allergens: String? = ""

    // Unused for now
    var nutrition_data: String? = "" // TODO: Possible to map and parse
    var nutrition_data_per: String? = ""
    var created: String = (System.currentTimeMillis() / 1000).toString()
}