package com.raiyansoft.eata.model.save

import com.google.gson.annotations.SerializedName
import com.raiyansoft.eata.model.privacy.Data

data class Save(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: com.raiyansoft.eata.model.save.Data,
    @SerializedName("status")
    var status: Boolean
)


data class SavePost(
    @SerializedName("code")
    var code: Int,
    @SerializedName("status")
    var status: Boolean
)