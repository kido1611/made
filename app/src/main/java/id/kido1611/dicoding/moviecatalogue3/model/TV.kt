package id.kido1611.dicoding.moviecatalogue3.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tv")
data class TV(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "poster")
    @SerializedName("poster_path")
    var poster: String,
    @ColumnInfo(name = "backdrop")
    @SerializedName("backdrop_path")
    var backdrop: String,
    @ColumnInfo(name = "title")
    @SerializedName("name")
    var title: String,
    @ColumnInfo(name = "overview")
    var overview: String,
    @ColumnInfo(name = "firstairdate")
    @SerializedName("first_air_date")
    var releaseDate: String,
    @ColumnInfo(name = "vote")
    @SerializedName("vote_average")
    var voteAverage: Double,
    @Ignore
    @SerializedName("episode_run_time")
    var runtime: List<Int>?
) : Parcelable {
    constructor(
        id: Int,
        poster: String,
        backdrop: String,
        title: String,
        overview: String,
        releaseDate: String,
        voteAverage: Double
    ) : this(
        id,
        poster,
        backdrop,
        title,
        overview,
        releaseDate,
        voteAverage,
        null
    )
}