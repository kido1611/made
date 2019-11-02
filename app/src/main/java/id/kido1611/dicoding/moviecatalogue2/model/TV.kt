package id.kido1611.dicoding.moviecatalogue2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TV(
    var title: String,
    var description: String,
    var voteAverage: Float,
    var poster: Int
) : Parcelable