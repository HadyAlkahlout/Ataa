package com.raiyansoft.eata.model.donation


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content(
    @SerializedName("date")
    var date: String,
    @SerializedName("case_id")
    var case_id: String,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("user_fcm")
    var user_fcm: String,
    @SerializedName("need_id")
    var need_id: String,
    @SerializedName("need_fcm")
    var need_fcm: String,
    @SerializedName("case_title")
    var case_title: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("total")
    var total: Int,
    @SerializedName("status")
    var status: Boolean
):Parcelable