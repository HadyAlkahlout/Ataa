package com.raiyansoft.eata.model.profile


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("country_code")
    var countryCode: String,
    @SerializedName("date")
    var date: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("image")
    var image: String,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("type")
    var type: String
)