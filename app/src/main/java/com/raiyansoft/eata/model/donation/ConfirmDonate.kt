package com.raiyansoft.eata.model.donation


import com.google.gson.annotations.SerializedName

data class ConfirmDonate(
    @SerializedName("id")
    var id: String,
    @SerializedName("total")
    var total: String
)