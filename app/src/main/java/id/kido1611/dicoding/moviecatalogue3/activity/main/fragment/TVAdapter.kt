package id.kido1611.dicoding.moviecatalogue3.activity.main.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.activity.detailtv.DetailTVActivity
import id.kido1611.dicoding.moviecatalogue3.model.TV
import kotlinx.android.synthetic.main.list_movie_item.view.*

class TVAdapter : RecyclerView.Adapter<TVAdapter.ListViewHolder>() {

    private val movieList = ArrayList<TV>()

    fun setData(items: ArrayList<TV>) {
        movieList.clear()
        movieList.addAll(items)
        notifyDataSetChanged()
    }

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
        fun bind(tv: TV) {
            with(itemView) {
                tv_title.text = tv.title
                val vote = resources.getString(R.string.vote, tv.voteAverage)
                tv_vote.text = vote
                val release = resources.getString(R.string.release, tv.releaseDate)
                tv_release.text = release
                Glide.with(itemView).load("https://image.tmdb.org/t/p/w92" + tv.poster)
                    .into(iv_poster)
            }
            itemView.setOnClickListener {
                val mIntent = Intent(itemView.context, DetailTVActivity::class.java)
                mIntent.putExtra(DetailTVActivity.TV_ITEM, tv.id)
                itemView.context.startActivity(mIntent)
            }
        }
    }
}