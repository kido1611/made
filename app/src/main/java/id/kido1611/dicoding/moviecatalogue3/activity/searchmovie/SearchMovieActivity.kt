package id.kido1611.dicoding.moviecatalogue3.activity.searchmovie

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.adapter.MovieAdapter
import id.kido1611.dicoding.moviecatalogue3.handler.ViewModelHandler
import id.kido1611.dicoding.moviecatalogue3.model.Movie
import id.kido1611.dicoding.moviecatalogue3.viewmodel.MovieListViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

class SearchMovieActivity : AppCompatActivity(), ViewModelHandler {

    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var adapter: MovieAdapter
    private lateinit var searchView: SearchView

    private var movieList = ArrayList<Movie>()

    private var isSuccess = false
    private var errorMessage = ""

    companion object{
        const val SUCCESS_STATE = "success_state"
        const val ERROR_MESSAGE_STATE = "error_message_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_movie)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""

        adapter = MovieAdapter(movieList)
        rv_list.adapter = adapter
        rv_list.layoutManager = LinearLayoutManager(this)

        movieListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MovieListViewModel::class.java)
        movieListViewModel.setViewModelHandler(this)
        movieListViewModel.getMovies().observe( this, Observer{
            if(it!=null){
                movieList.clear()
                movieList.addAll(it)
                adapter.notifyDataSetChanged()

                if(it.size == 0){
                    onFailure(resources.getString(R.string.search_not_found))
                }else{
                    onSuccess()
                }
            }
        })

        if(savedInstanceState !=null){
            isSuccess = savedInstanceState.getBoolean(SUCCESS_STATE)
            errorMessage = savedInstanceState.getString(ERROR_MESSAGE_STATE)

            if(!isSuccess){
                onFailure(errorMessage)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ERROR_MESSAGE_STATE, errorMessage)
        outState.putBoolean(SUCCESS_STATE, isSuccess)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_movie_menu, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
        searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                movieListViewModel.searchMovies(query?:"")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.requestFocus()
        searchView.isFocusable = true
        searchView.requestFocusFromTouch()
        searchView.isIconified = false

        movieListViewModel.getSearchMovieText().observe(this, Observer {
            if(it!=null){
                searchView.setQuery(it, false)
                searchView.isFocusable = false
                searchView.clearFocus()
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onInit() {
        progressBar.visibility = View.VISIBLE
        rv_list.visibility = View.GONE
        layout_message.visibility = View.GONE
    }
    override fun onSuccess() {
        progressBar.visibility = View.GONE
        rv_list.visibility = View.VISIBLE
        layout_message.visibility = View.GONE

        isSuccess = true
    }
    override fun onFailure(message: String) {
        progressBar.visibility = View.GONE
        rv_list.visibility = View.GONE
        layout_message.visibility = View.VISIBLE
        btn_reload.visibility = View.GONE

        tv_message.text = message

        errorMessage = message
        isSuccess = false
    }
}
