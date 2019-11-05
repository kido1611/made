package id.kido1611.dicoding.moviecatalogue3.activity.detailmovie

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.db.MovieDatabase
import id.kido1611.dicoding.moviecatalogue3.handler.ViewModelHandler
import id.kido1611.dicoding.moviecatalogue3.model.Movie
import id.kido1611.dicoding.moviecatalogue3.viewmodel.MovieViewModel
import id.kido1611.dicoding.moviecatalogue3.widget.FavoriteMovieWidget
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailMovieActivity : AppCompatActivity(), ViewModelHandler {

    companion object {
        const val MOVIE_ITEM = "movie_item"
        const val MOVIE_STATE = "movie"
        const val SUCCESS_STATE = "success"
        const val ERROR_MESSAGE_STATE = "error_message"
        const val FAVORITE_STATE = "favorite"
    }

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieDatabase: MovieDatabase

    private var movieId: Int = 0
    private var movie: Movie? = null
    private var successLoad = false
    private var errorMessage = ""
    private var isFavorite = false
    private var menuItem: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieId = intent.getIntExtra(MOVIE_ITEM, 0)

        setContentView(R.layout.activity_detail_movie)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        movieDatabase = MovieDatabase.getInstance(applicationContext) as MovieDatabase

        movieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MovieViewModel::class.java)
        movieViewModel.setViewModelHandler(this)
        movieViewModel.getMovie().observe(this, Observer {
            if (it != null) {
                movie = it
                showMovie(it)

                onSuccess()

                updateFavorite()
            }
        })

        btn_reload.setOnClickListener {
            loadData()
        }

        title = ""

        if (savedInstanceState == null) {
            loadData()
        } else {
            movieId = savedInstanceState.getInt(MOVIE_ITEM)
            successLoad = savedInstanceState.getBoolean(SUCCESS_STATE)
            errorMessage = savedInstanceState.getString(ERROR_MESSAGE_STATE)
            movie = savedInstanceState.getParcelable(MOVIE_STATE)
            isFavorite = savedInstanceState.getBoolean(FAVORITE_STATE)

            if (successLoad) {
                showMovie(movie!!)
                onSuccess()
            } else {
                onFailure(errorMessage)
            }

            updateFavorite()
        }

        favoriteState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MOVIE_ITEM, movieId)
        outState.putParcelable(MOVIE_STATE, movie)
        outState.putBoolean(SUCCESS_STATE, successLoad)
        outState.putString(ERROR_MESSAGE_STATE, errorMessage)
        outState.putBoolean(FAVORITE_STATE, isFavorite)
    }

    private fun loadData() {
        movieViewModel.setMovie(movieId, resources.getString(R.string.data_language))
    }

    private fun showMovie(movie: Movie) {
        layout_movie.visibility = View.VISIBLE

        title = movie.title

        tv_title.text = movie.title
        if (movie.overview.isBlank()) {
            tv_description.text = resources.getString(R.string.not_available)
        } else {
            tv_description.text = movie.overview
        }
        tv_release.text = movie.releaseDate
        tv_vote.text = movie.voteAverage.toString()
        tv_runtime.text = movie.runtime.toString()
        Glide.with(this@DetailMovieActivity).load("https://image.tmdb.org/t/p/w185" + movie.poster)
            .into(iv_poster)
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movie.backdrop)
            .into(app_bar_image)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        updateFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_favorite -> {
                if (isFavorite) {
                    removeFromFavorite()
                } else {
                    addToFavorite()
                }
                isFavorite = !isFavorite
                updateFavorite()

                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addToFavorite() {
        GlobalScope.launch {
            movieDatabase.movieDAO().insertMovie(movie!!)

            isFavorite = true
            withContext(Dispatchers.Main) {
                updateFavorite()
                Toast.makeText(
                    this@DetailMovieActivity,
                    resources.getString(R.string.favorite_add_success),
                    Toast.LENGTH_SHORT
                ).show()
                FavoriteMovieWidget.updateWidget(this@DetailMovieActivity)
            }
        }
    }

    private fun removeFromFavorite() {
        GlobalScope.launch {
            movieDatabase.movieDAO().deleteMovieById(movieId)
            isFavorite = false
            withContext(Dispatchers.Main) {
                updateFavorite()
                Toast.makeText(
                    this@DetailMovieActivity,
                    resources.getString(R.string.favorite_remove_success),
                    Toast.LENGTH_SHORT
                ).show()
                FavoriteMovieWidget.updateWidget(this@DetailMovieActivity)
            }
        }
    }

    private fun updateFavorite() {
        if (isFavorite) {
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
        } else {
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
        }
    }

    private fun favoriteState() {
        GlobalScope.launch {
            val movieFavorite = movieDatabase.movieDAO().findById(movieId)
            isFavorite = movieFavorite != null

            withContext(Dispatchers.Main) {
                updateFavorite()
            }
        }
    }

    override fun onSuccess() {
        successLoad = true

        appbar.setExpanded(true)
        progressBar.visibility = View.GONE
        layout_movie.visibility = View.VISIBLE
        layout_message.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        successLoad = false
        errorMessage = message

        progressBar.visibility = View.GONE
        layout_movie.visibility = View.GONE
        layout_message.visibility = View.VISIBLE
        tv_message.text = errorMessage
        appbar.setExpanded(false)
    }

    override fun onInit() {
        progressBar.visibility = View.VISIBLE
        layout_movie.visibility = View.GONE
        layout_message.visibility = View.GONE
        appbar.setExpanded(false)
    }
}