package com.joshuatabakhoff.caddik.network.model

class Product {
    var product_name = ""
    var generic_name = ""
    var image_url    = ""
    var brand_owner  = ""
    var origins      = ""
    var labels       = ""
    var quantity     = ""

    var ingredients = listOf<Ingredient>()

    var ingredients_text = ""
    var ingredients_text_with_allergens = ""

    // Unused for now
    var nutrition_data = "" // TODO: Possible to map and parse
    var nutrition_data_per = ""
}