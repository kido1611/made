package id.kido1611.dicoding.moviecatalogue3.activity.detailmovie

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.model.Movie
import id.kido1611.dicoding.moviecatalogue3.network.RetrofitBuilder
import id.kido1611.dicoding.moviecatalogue3.network.TheMovieDBServices
import id.kido1611.dicoding.moviecatalogue3.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_detail_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        var MOVIE_ITEM = "movie_item"
    }

    private lateinit var call: Call<Movie>

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieId = intent.getIntExtra(MOVIE_ITEM, 0)

        setContentView(R.layout.activity_detail_movie)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        movieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MovieViewModel::class.java)
        movieViewModel.getMovie().observe(this, Observer {
            if (it != null) {
                progressBar.visibility = View.GONE
                showMovie(it)
            }
        })

        val service = RetrofitBuilder.createService(TheMovieDBServices::class.java)
        call = service.GetMovie(movieId, resources.getString(R.string.data_language))

        loadData()
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE

        if (call.isExecuted) {
            return
        }
        call.enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                progressBar.visibility = View.GONE
            }

            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                progressBar.visibility = View.GONE
                val movie = response.body() as Movie
                movieViewModel.setMovie(movie)

            }

        })
    }

    private fun showMovie(movie: Movie) {
        layout_movie.visibility = View.VISIBLE

        tv_title.text = movie.title
        tv_description.text = movie.overview
        tv_release.text = movie.releaseDate
        tv_vote.text = movie.voteAverage.toString()
        tv_runtime.text = movie.runtime.toString()
        Glide.with(this@DetailMovieActivity).load("https://image.tmdb.org/t/p/w185" + movie.poster)
            .into(iv_poster)
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
