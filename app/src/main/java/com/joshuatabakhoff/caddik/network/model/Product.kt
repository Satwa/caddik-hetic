package com.joshuatabakhoff.caddik.network.model

class Product {
    var product_name = ""
    var generic_name = ""
    var image_url    = ""
    var brand_owner  = ""
    var origins: String?      = ""
    var labels: String?       = ""
    var quantity: String?     = ""

    var ingredients = listOf<Ingredient>()

    var ingredients_text: String? = ""
    var ingredients_text_with_allergens: String? = ""

    // Unused for now
    var nutrition_data: String? = "" // TODO: Possible to map and parse
    var nutrition_data_per: String? = ""
}