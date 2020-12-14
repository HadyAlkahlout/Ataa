package com.raiyansoft.eata.model.privacy


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("privacy")
    var privacy: String
)