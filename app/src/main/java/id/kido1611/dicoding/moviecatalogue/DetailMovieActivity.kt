package id.kido1611.dicoding.moviecatalogue

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import id.kido1611.dicoding.moviecatalogue.model.Movie
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        var MOVIE_ITEM = "movie_item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieItem: Movie = intent.getParcelableExtra(MOVIE_ITEM)

        setContentView(R.layout.activity_detail_movie)
        setSupportActionBar(toolbar)

        supportActionBar?.title = movieItem.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tv_title.text = movieItem.title
        tv_description.text = movieItem.description
        tv_release.text = "Rilis tanggal: " + movieItem.releaseDate
        tv_runtime.text = "Durasi: " + movieItem.runtime + " menit"
        tv_vote.text = "Vote: " + movieItem.voteAverage
        iv_poster.setImageResource(movieItem.poster)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
