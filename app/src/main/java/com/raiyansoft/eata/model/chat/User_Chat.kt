package com.raiyansoft.eata.model.chat

data class User_Chat(
    var Username: String,
    var channelId: String,
    var Token_fcm: String

) {
    constructor() : this("", "", "")
}