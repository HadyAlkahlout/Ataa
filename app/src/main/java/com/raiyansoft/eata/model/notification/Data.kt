package com.raiyansoft.eata.model.notification


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("count_total")
    var countTotal: Int,
    @SerializedName("data")
    var `data`: ArrayList<Content>,
    @SerializedName("pages")
    var pages: Int
)