package id.kido1611.dicoding.moviecatalogue

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import id.kido1611.dicoding.moviecatalogue.model.Movie
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var dataTitle: Array<String>
    private lateinit var dataRelease: Array<String>
    private lateinit var dataVote: Array<String>
    private lateinit var dataDescription: Array<String>
    private lateinit var dataRuntime: Array<String>
    private lateinit var dataPoster: TypedArray

    private var movies = arrayListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        adapter = MovieAdapter(this)
        lv_list.adapter = adapter
        lv_list.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val mIntent = Intent(this, DetailMovieActivity::class.java)
                mIntent.putExtra(DetailMovieActivity.MOVIE_ITEM, movies[position])
                startActivity(mIntent)

            }

        prepare()
        addItem()
    }

    private fun prepare() {
        dataTitle = resources.getStringArray(R.array.data_title)
        dataDescription = resources.getStringArray(R.array.data_description)
        dataRelease = resources.getStringArray(R.array.data_release)
        dataVote = resources.getStringArray(R.array.data_vote)
        dataRuntime = resources.getStringArray(R.array.data_runtime)
        dataPoster = resources.obtainTypedArray(R.array.data_poster)
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
        adapter.movies = movies
    }
}
