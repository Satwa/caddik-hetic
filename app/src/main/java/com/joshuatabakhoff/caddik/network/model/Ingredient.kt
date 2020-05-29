package com.joshuatabakhoff.caddik.network.model

class Ingredient {
    var id = ""
    var rank = 0

    var text = ""

    var has_sub_ingredients = ""
    var percent_min: Int? = 0
    var percent_max: Int? = 100

    var vegetarian: String? = "yes"
    var vegan: String? = "yes"
}