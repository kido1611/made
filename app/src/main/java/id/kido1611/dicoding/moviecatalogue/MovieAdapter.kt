package id.kido1611.dicoding.moviecatalogue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import id.kido1611.dicoding.moviecatalogue.model.Movie
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter internal constructor(private val context: Context) : BaseAdapter() {
    internal var movies = arrayListOf<Movie>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        }
        val viewHolder = ViewHolder(itemView as View)
        val movie = getItem(position)
        viewHolder.bind(movie)

        return itemView
    }

    override fun getItem(position: Int): Movie = movies[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = movies.size

    inner class ViewHolder internal constructor(private val view: View) {
        fun bind(movie: Movie) {
            with(view) {
                iv_poster.setImageResource(movie.poster)
                tv_title.text = movie.title
                tv_release.text = "Rilis tanggal " + movie.releaseDate
                tv_vote.text = "Vote: " + movie.voteAverage.toString()
            }
        }
    }
}