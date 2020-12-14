package com.raiyansoft.eata.model.contact


import com.google.gson.annotations.SerializedName

data class ContactUs(
    @SerializedName("code")
    var code: Int,
    @SerializedName("status")
    var status: Boolean
)