package com.joshuatabakhoff.caddik.model

class Ingredient {
    var id = ""
    var rank = 0

    var text = ""

    var has_sub_ingredients = ""
    var percent_min: Double? = 0.0
    var percent_max: Double? = 100.0

    var vegetarian: String? = "yes"
    var vegan: String? = "yes"
}