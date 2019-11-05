package id.kido1611.dicoding.moviecataloguefavorite.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var id: Int,
    var poster: String,
    var backdrop: String,
    var title: String,
    var overview: String,
    var releaseDate: String,
    var voteAverage: Double,
    var runtime: Int
) : Parcelable