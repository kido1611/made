package id.kido1611.dicoding.moviecataloguefavorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.kido1611.dicoding.moviecataloguefavorite.R
import id.kido1611.dicoding.moviecataloguefavorite.model.Movie
import kotlinx.android.synthetic.main.list_movie_item.view.*

class MovieAdapter internal constructor(private var movieList: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_movie_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                tv_title.text = movie.title
                val release = resources.getString(R.string.release, movie.releaseDate)
                tv_release.text = release
                val vote = resources.getString(R.string.vote, movie.voteAverage)
                tv_vote.text = vote
                if(movie.poster!=null){
                    Glide.with(itemView).load("https://image.tmdb.org/t/p/w92" + movie.poster)
                        .into(iv_poster)
                }
            }
            itemView.setOnClickListener {

            }
        }
    }
}