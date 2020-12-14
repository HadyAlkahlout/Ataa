package com.raiyansoft.eata.model.question


import com.google.gson.annotations.SerializedName

data class QuestionAnswer(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)