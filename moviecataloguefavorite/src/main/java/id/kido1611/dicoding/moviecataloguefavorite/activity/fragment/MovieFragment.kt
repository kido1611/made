package id.kido1611.dicoding.moviecataloguefavorite.activity.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.kido1611.dicoding.moviecataloguefavorite.R
import id.kido1611.dicoding.moviecataloguefavorite.adapter.MovieAdapter
import id.kido1611.dicoding.moviecataloguefavorite.model.Movie
import id.kido1611.dicoding.moviecataloguefavorite.viewmodel.MovieListViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    private lateinit var adapter: MovieAdapter
    private lateinit var movieListViewModel: MovieListViewModel

    private val movieList = ArrayList<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MovieAdapter(movieList)
        rv_list.adapter = adapter
        rv_list.layoutManager = LinearLayoutManager(context)
        rv_list.setHasFixedSize(true)

        movieListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MovieListViewModel::class.java)
        movieListViewModel.getData().observe(viewLifecycleOwner, Observer{
            movieList.clear()
            movieList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        movieListViewModel.setData(context!!)
    }

}
