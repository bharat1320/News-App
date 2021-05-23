package com.example.wikkipiedia

data class News(
    val title : String,
    val author : String,
    val url : String,
    val imageUrl : String
    )

val Country = arrayListOf(
    " India - in ",
    " USA - us ",
    " Australia - au ",
    " Russia - ru ",
    " France - fr ",
    " United Kingdom - gb "
)

val Category = arrayListOf(
    " business ",
    " entertainment ",
    " general ",
    " health ",
    " science ",
    " sports ",
    " technology "
)