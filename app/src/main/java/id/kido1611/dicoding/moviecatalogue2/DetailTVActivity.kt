package id.kido1611.dicoding.moviecatalogue2

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import id.kido1611.dicoding.moviecatalogue2.model.TV
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailTVActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)
        val movieItem: TV = intent.getParcelableExtra(DetailMovieActivity.MOVIE_ITEM)

        setContentView(R.layout.activity_detail_tv)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tv_title.text = movieItem.title
        tv_description.text = movieItem.description
        tv_vote.text = movieItem.voteAverage.toString()
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