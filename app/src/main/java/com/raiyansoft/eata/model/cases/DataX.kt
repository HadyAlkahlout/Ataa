package com.raiyansoft.eata.model.cases


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("details")
    var details: String?,
    @SerializedName("id")
    var id: Int,
    @SerializedName("images")
    var images: ArrayList<Image>,
    @SerializedName("title")
    var title: String?,
    @SerializedName("user_name")
    var user_name: String?,
    @SerializedName("accepted")
    var accepted: Boolean?,
    @SerializedName("user_id")
    var user_id: String?,
    @SerializedName("user_mobile")
    var user_mobile: String?,
    @SerializedName("user_fcm_token")
    var user_fcm_token: String?,
    @SerializedName("cat_title")
    var cat_title: String?,
    @SerializedName("cat_id")
    var cat_id: String?,
    @SerializedName("total")
    var total: String?
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        TODO("images"),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(details)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(user_name)
        parcel.writeValue(accepted)
        parcel.writeString(user_id)
        parcel.writeString(user_mobile)
        parcel.writeString(user_fcm_token)
        parcel.writeString(cat_title)
        parcel.writeString(cat_id)
        parcel.writeString(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataX> {
        override fun createFromParcel(parcel: Parcel): DataX {
            return DataX(parcel)
        }

        override fun newArray(size: Int): Array<DataX?> {
            return arrayOfNulls(size)
        }
    }
}