package com.raiyansoft.eata.model.categories


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("cases")
    var cases: Int,
    @SerializedName("id")
    var id: Long,
    @SerializedName("subCategory")
    var subCategory: Int,
    @SerializedName("image")
    var image: String,
    @SerializedName("title")
    var title: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cases)
        parcel.writeLong(id)
        parcel.writeInt(subCategory)
        parcel.writeString(image)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Content> {
        override fun createFromParcel(parcel: Parcel): Content {
            return Content(parcel)
        }

        override fun newArray(size: Int): Array<Content?> {
            return arrayOfNulls(size)
        }
    }
}