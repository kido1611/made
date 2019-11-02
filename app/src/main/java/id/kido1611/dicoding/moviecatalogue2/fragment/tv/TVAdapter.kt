package id.kido1611.dicoding.moviecatalogue2.fragment.tv

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.kido1611.dicoding.moviecatalogue2.DetailMovieActivity
import id.kido1611.dicoding.moviecatalogue2.DetailTVActivity
import id.kido1611.dicoding.moviecatalogue2.R
import id.kido1611.dicoding.moviecatalogue2.model.TV
import kotlinx.android.synthetic.main.grid_movie_item.view.*

class TVAdapter internal constructor(private var tvLists: ArrayList<TV>) :
    RecyclerView.Adapter<TVAdapter.GridTVViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridTVViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.grid_movie_item, parent, false)
        return GridTVViewHolder(view)
    }

    override fun getItemCount(): Int = tvLists.size

    override fun onBindViewHolder(holder: GridTVViewHolder, position: Int) {
        holder.bind(tvLists[position])
    }

    class GridTVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tv: TV) {
            with(itemView) {
                iv_poster.setImageResource(tv.poster)
            }
            itemView.setOnClickListener {
                val mIntent = Intent(itemView.context, DetailTVActivity::class.java)
                mIntent.putExtra(DetailMovieActivity.MOVIE_ITEM, tv)
                itemView.context.startActivity(mIntent)
            }
        }
    }
}