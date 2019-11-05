package id.kido1611.dicoding.moviecatalogue3.db

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import id.kido1611.dicoding.moviecatalogue3.model.Movie

class FavoriteMovieProvider : ContentProvider() {

    private lateinit var movieDatabase: MovieDatabase

    companion object{

        const val AUTHORITY = "id.kido1611.dicoding.moviecatalogue3"
        const val SCHEME = "content"
        const val MOVIE_TABLE = "movie"
        const val TV_TABLE = "tv"
        const val MOVIE_DIR = 1
        const val TV_DIR = 2

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(MOVIE_TABLE)
            .build()

        init {
            sUriMatcher.addURI(AUTHORITY, MOVIE_TABLE, MOVIE_DIR )
            sUriMatcher.addURI(AUTHORITY, TV_TABLE, TV_DIR )
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        val data = Movie(
            id = values.getAsInteger("id"),
            poster = values.getAsString("poster"),
            backdrop = values.getAsString("backdrop"),
            title = values.getAsString("title"),
            overview = values.getAsString("overview"),
            releaseDate = values.getAsString("releaseDate"),
            voteAverage = values.getAsDouble("vote"),
            runtime = values.getAsInteger("runtime")
        )
        val result = movieDatabase.movieDAO().insertMovie(data)
        context.contentResolver.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, result)
    }

    override fun onCreate(): Boolean {
        movieDatabase = MovieDatabase.getInstance(context.applicationContext)!!

        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(sUriMatcher.match(uri)){
            MOVIE_DIR -> {
                movieDatabase.movieDAO().getAllMoviesProviders()
            }
            TV_DIR -> {
                movieDatabase.tvDAO().getAllTVProviders()
            }
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }
}
