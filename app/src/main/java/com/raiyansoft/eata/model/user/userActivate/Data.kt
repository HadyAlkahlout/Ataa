package com.raiyansoft.eata.model.user.userActivate


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("country_code")
    var countryCode: String,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("user_id")
    var userId: Int
)