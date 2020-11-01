package com.jobsity.tvseries.domain.model

import android.os.Parcel
import android.os.Parcelable

data class TvShow (
    val id: Int,
    val name: String,
    val posterHigh: String?,
    val posterMedium: String,
    val time: String,
    val days: List<String>,
    val genre: List<String>?,
    val summary: String,
    val rating: String?,
    val premier: String?,
    var isFavorite: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(posterHigh)
        parcel.writeString(posterMedium)
        parcel.writeString(time)
        parcel.writeStringList(days)
        parcel.writeStringList(genre)
        parcel.writeString(summary)
        parcel.writeString(rating)
        parcel.writeString(premier)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TvShow> {
        override fun createFromParcel(parcel: Parcel): TvShow {
            return TvShow(parcel)
        }

        override fun newArray(size: Int): Array<TvShow?> {
            return arrayOfNulls(size)
        }
    }
}

