package id.kido1611.dicoding.moviecatalogue2

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import id.kido1611.dicoding.moviecatalogue2.model.Movie
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        var MOVIE_ITEM = "movie_item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieItem: Movie = intent.getParcelableExtra(MOVIE_ITEM)

        setContentView(R.layout.activity_detail_movie)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tv_title.text = movieItem.title
        tv_description.text = movieItem.description
        tv_release.text = movieItem.releaseDate
        tv_vote.text = movieItem.voteAverage.toString()
        tv_runtime.text = movieItem.runtime.toString()
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
