package com.jobsity.tvseries.domain.model

import android.os.Parcel
import android.os.Parcelable

class TvShowEpisode (
    val id: Int,
    val name: String,
    val season: Int,
    val number: Int,
    val mediumImage: String?,
    val originalImage: String?,
    val summary: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(season)
        parcel.writeInt(number)
        parcel.writeString(mediumImage)
        parcel.writeString(originalImage)
        parcel.writeString(summary)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TvShowEpisode> {
        override fun createFromParcel(parcel: Parcel): TvShowEpisode {
            return TvShowEpisode(parcel)
        }

        override fun newArray(size: Int): Array<TvShowEpisode?> {
            return arrayOfNulls(size)
        }
    }
}