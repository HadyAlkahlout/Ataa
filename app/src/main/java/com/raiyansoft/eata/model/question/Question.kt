package com.raiyansoft.eata.model.question


import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("answer")
    var answer: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("question")
    var question: String
)