package id.kido1611.dicoding.moviecatalogue2.fragment.movie


import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.kido1611.dicoding.moviecatalogue2.R
import id.kido1611.dicoding.moviecatalogue2.model.Movie
import kotlinx.android.synthetic.main.fragment_movies.*

/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment : Fragment() {

    private lateinit var adapter: MovieAdapter
    private var movies = arrayListOf<Movie>()

    private lateinit var dataTitle: Array<String>
    private lateinit var dataRelease: Array<String>
    private lateinit var dataVote: Array<String>
    private lateinit var dataDescription: Array<String>
    private lateinit var dataRuntime: Array<String>
    private lateinit var dataPoster: TypedArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MovieAdapter(movies)

        rv_list.adapter = adapter
        rv_list.layoutManager = LinearLayoutManager(context)

        prepare()
        addItem()

    }

    private fun prepare() {
        dataTitle = resources.getStringArray(R.array.movies_title)
        dataDescription = resources.getStringArray(R.array.movies_description)
        dataRelease = resources.getStringArray(R.array.movies_release)
        dataVote = resources.getStringArray(R.array.movies_vote)
        dataRuntime = resources.getStringArray(R.array.movies_runtime)
        dataPoster = resources.obtainTypedArray(R.array.movies_poster)
    }

    private fun addItem() {
        for (position in dataTitle.indices) {
            val movie = Movie(
                dataTitle[position],
                dataDescription[position],
                dataRelease[position],
                dataVote[position].toFloat(),
                dataRuntime[position].toInt(),
                dataPoster.getResourceId(position, -1)
            )
            movies.add(movie)
        }

        adapter.notifyDataSetChanged()
    }

}
