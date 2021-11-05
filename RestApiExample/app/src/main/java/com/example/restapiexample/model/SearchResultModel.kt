package com.example.restapiexample.model

import android.graphics.Bitmap

data class SearchResultModel(
    var image: Bitmap?,
    var name: String?,
    var price: String?
)