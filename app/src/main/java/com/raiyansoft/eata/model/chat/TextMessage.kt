package com.raiyansoft.eata.model.chat

import java.util.*

data class TextMessage(
    var caseId: String,
    var date: String,
    val from: String,
    val message: String,
    var to: String,
    var toName: String
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "")
}