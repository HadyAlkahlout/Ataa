package com.raiyansoft.eata.model.cases


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("count_total")
    var countTotal: Int,
    @SerializedName("data")
    var `data`: ArrayList<DataX>,
    @SerializedName("nextPageUrl")
    var nextPageUrl: Any,
    @SerializedName("pages")
    var pages: Int
)