package com.raiyansoft.eata.model.categories


import com.google.gson.annotations.SerializedName

data class Categories(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)