package id.kido1611.dicoding.moviecatalogue3.activity.detailtv

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
import id.kido1611.dicoding.moviecatalogue3.model.TV
import id.kido1611.dicoding.moviecatalogue3.viewmodel.TVViewModel
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailTVActivity : AppCompatActivity(), ViewModelHandler {

    companion object {
        const val MOVIE_ITEM = "movie_item"
        const val MOVIE_STATE = "movie"
        const val SUCCESS_STATE = "success"
        const val ERROR_MESSAGE_STATE = "error_message"
        const val FAVORITE_STATE = "favorite"
    }

    private lateinit var tvViewModel: TVViewModel
    private lateinit var movieDatabase: MovieDatabase

    private var tvId: Int = 0
    private var tv: TV? = null
    private var successLoad = false
    private var errorMessage = ""
    private var isFavorite = false
    private var menuItem: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvId = intent.getIntExtra(MOVIE_ITEM, 0)

        setContentView(R.layout.activity_detail_movie)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textView4.text = resources.getString(R.string.field_tv_runtime)

        movieDatabase = MovieDatabase.getInstance(applicationContext) as MovieDatabase

        tvViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(TVViewModel::class.java)
        tvViewModel.setViewModelHandler(this)
        tvViewModel.getTV().observe(this, Observer {
            if (it != null) {
                tv = it
                showMovie(it)

                onSuccess()

                updateFavorite()
            }
        })

        title = ""

        if (savedInstanceState == null) {
            loadData()
        } else {
            tvId = savedInstanceState.getInt(MOVIE_ITEM)
            tv = savedInstanceState.getParcelable(MOVIE_STATE)
            successLoad = savedInstanceState.getBoolean(SUCCESS_STATE)
            errorMessage = savedInstanceState.getString(ERROR_MESSAGE_STATE)
            isFavorite = savedInstanceState.getBoolean(FAVORITE_STATE)

            if (successLoad) {
                showMovie(tv!!)
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
        outState.putInt(MOVIE_ITEM, tvId)
        outState.putParcelable(MOVIE_STATE, tv)
        outState.putBoolean(SUCCESS_STATE, successLoad)
        outState.putString(ERROR_MESSAGE_STATE, errorMessage)
        outState.putBoolean(FAVORITE_STATE, isFavorite)
    }

    private fun loadData() {
        tvViewModel.setTV(tvId, resources.getString(R.string.data_language))
    }

    private fun showMovie(movie: TV) {
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
        tv_runtime.text = movie.runtime!![0].toString()
        Glide.with(this@DetailTVActivity).load("https://image.tmdb.org/t/p/w185" + movie.poster)
            .into(iv_poster)
        Glide.with(this@DetailTVActivity).load("https://image.tmdb.org/t/p/w500" + movie.backdrop)
            .into(app_bar_image)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
            movieDatabase.tvDAO().insertTV(tv!!)
            isFavorite = true
            withContext(Dispatchers.Main) {
                updateFavorite()
                Toast.makeText(
                    this@DetailTVActivity,
                    resources.getString(R.string.favorite_add_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun removeFromFavorite() {
        GlobalScope.launch {
            movieDatabase.tvDAO().deleteTVById(tvId)
            isFavorite = false
            withContext(Dispatchers.Main) {
                updateFavorite()
                Toast.makeText(
                    this@DetailTVActivity,
                    resources.getString(R.string.favorite_remove_success),
                    Toast.LENGTH_SHORT
                ).show()
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
            val movieFavorite = movieDatabase.tvDAO().findById(tvId)
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
