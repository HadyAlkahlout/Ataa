package com.raiyansoft.eata.model.contact

import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("name")
    val name: String,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("message")
    val message: String
)