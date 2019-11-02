package id.kido1611.dicoding.moviecatalogue2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var title: String,
    var description: String,
    var releaseDate: String,
    var voteAverage: Float,
    var runtime: Int,
    var poster: Int
) : Parcelable