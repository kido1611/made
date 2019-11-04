package id.kido1611.dicoding.moviecatalogue3.activity.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.adapter.MovieAdapter
import id.kido1611.dicoding.moviecatalogue3.db.MovieDatabase
import id.kido1611.dicoding.moviecatalogue3.handler.ViewModelHandler
import id.kido1611.dicoding.moviecatalogue3.model.Movie
import id.kido1611.dicoding.moviecatalogue3.viewmodel.MovieListViewModel
import id.kido1611.dicoding.moviecatalogue3.activity.searchmovie.SearchMovieActivity
import kotlinx.android.synthetic.main.fragment_movie.*


/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment(), ViewModelHandler {

    private lateinit var adapter: MovieAdapter
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var movieDatabase: MovieDatabase

    private var movieList = ArrayList<Movie>()
    private var successLoad = false
    private var errorMessage = ""

    private var isFavorite = false

    companion object {
        const val MOVIES_STATE = "movie"
        const val SUCCESS_STATE = "success"
        const val ERROR_MESSAGE_STATE = "error_message"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null) {
            isFavorite = arguments!!.getBoolean("favorite", false)
        }
        adapter = MovieAdapter(movieList)
        rv_list.adapter = adapter
        rv_list.layoutManager = LinearLayoutManager(activity)

        movieDatabase = MovieDatabase.getInstance(context!!.applicationContext) as MovieDatabase

        movieListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MovieListViewModel::class.java)
        movieListViewModel.setViewModelHandler(this)
        movieListViewModel.getMovies().observe(viewLifecycleOwner, Observer {
            if (it != null) {

                movieList.clear()
                movieList.addAll(it)
                adapter.notifyDataSetChanged()

                onSuccess()
            }
        })

        if (savedInstanceState == null) {
            loadData()
        } else {
            errorMessage = savedInstanceState.getString(ERROR_MESSAGE_STATE)
            successLoad = savedInstanceState.getBoolean(SUCCESS_STATE)

            if (successLoad) {
                movieList.clear()
                movieList.addAll(savedInstanceState.getParcelableArrayList(MOVIES_STATE))
                adapter.notifyDataSetChanged()

                onSuccess()
            } else {
                onFailure(errorMessage)
            }
        }

        btn_reload.setOnClickListener {
            loadData()
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if(isFavorite) {
            inflater.inflate(R.menu.movie_menu, menu)
        }
        else{
            inflater.inflate(R.menu.search_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_reload -> {
                loadData()
                return true
            }
            R.id.menu_search -> {
                val intent = Intent(context, SearchMovieActivity::class.java)
                context?.startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(MOVIES_STATE, movieList)
        outState.putBoolean(SUCCESS_STATE, successLoad)
        outState.putString(ERROR_MESSAGE_STATE, errorMessage)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showList(state: Boolean) {
        if (state) {
            rv_list.visibility = View.VISIBLE
        } else {
            rv_list.visibility = View.GONE
        }
    }

    private fun showError(state: Boolean, message: String) {
        if (state) {
            layout_message.visibility = View.VISIBLE
        } else {
            layout_message.visibility = View.GONE
        }
        tv_message.text = message
    }

    private fun loadData() {
        if (isFavorite) {
            movieListViewModel.setFavoriteMovies(movieDatabase.movieDAO())
        } else {
            movieListViewModel.setMovies()
        }
    }

    override fun onFailure(message: String) {
        showLoading(false)
        showList(false)
        showError(true, message)

        successLoad = false
        errorMessage = message
    }

    override fun onSuccess() {
        showLoading(false)
        showList(true)
        showError(false, "")
        successLoad = true
    }

    override fun onInit() {
        showLoading(true)
        showList(false)
        showError(false, "")
    }

}
