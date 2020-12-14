package com.raiyansoft.eata.model.notification


import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("title")
    var title: String
)