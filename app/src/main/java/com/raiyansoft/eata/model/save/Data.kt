package com.raiyansoft.eata.model.save


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Data(
    @SerializedName("count_total")
    val count_Total: Long,

    @SerializedName("nextPageUrl")
    val nextPageURL: Any? = null,
    @SerializedName("pages")
    val pages: Long,
    @SerializedName("data")
    val data: List<Cases2>
)

@Parcelize
data class Cases2 (
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,

    @SerializedName("cat_title")
    val cat_title: String,
    @SerializedName("cat_id")
    val cat_id: Long,
    @SerializedName("details")
    val details: String,
    @SerializedName("total")
    val total: String,
    @SerializedName("images")
    val images: ArrayList<Image>
):Parcelable

@Parcelize
data class Image (
    @SerializedName("image")
    val image: String
):Parcelable


data class PostSave(
    @SerializedName("id")
    val id: Long
)