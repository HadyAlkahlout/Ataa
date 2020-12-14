package com.raiyansoft.eata.model.setting


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("force_close")
    var forceClose: String,
    @SerializedName("android_version")
    var android_version: String,
    @SerializedName("ios_version")
    var ios_version: String,
    @SerializedName("force_update")
    var forceUpdate: String
)