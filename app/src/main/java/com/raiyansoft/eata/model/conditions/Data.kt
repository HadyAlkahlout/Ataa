package com.raiyansoft.eata.model.conditions


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("conditions")
    var conditions: String
)