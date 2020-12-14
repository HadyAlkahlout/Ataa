package com.raiyansoft.eata.model.cases


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("image")
    var image: String,
    @SerializedName("id")
    var id: String
)