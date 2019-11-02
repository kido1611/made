package id.kido1611.dicoding.moviecatalogue3.activity.detailtv

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.model.TV
import id.kido1611.dicoding.moviecatalogue3.network.RetrofitBuilder
import id.kido1611.dicoding.moviecatalogue3.network.TheMovieDBServices
import id.kido1611.dicoding.moviecatalogue3.viewmodel.TVViewModel
import kotlinx.android.synthetic.main.activity_detail_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTVActivity : AppCompatActivity() {

    companion object {
        var TV_ITEM = "tv_item"
    }

    private lateinit var call: Call<TV>

    private lateinit var tvViewModel: TVViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tvId = intent.getIntExtra(TV_ITEM, 0)

        setContentView(R.layout.activity_detail_movie)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textView4.text = resources.getString(R.string.field_tv_runtime)

        tvViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(TVViewModel::class.java)
        tvViewModel.getTV().observe(this, Observer {
            if (it != null) {
                progressBar.visibility = View.GONE
                showMovie(it)
            }
        })

        val service = RetrofitBuilder.createService(TheMovieDBServices::class.java)
        call = service.GetTV(tvId, resources.getString(R.string.data_language))

        loadData()
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE
        if (call.isExecuted) {
            return
        }
        call.enqueue(object : Callback<TV> {
            override fun onFailure(call: Call<TV>, t: Throwable) {
                progressBar.visibility = View.GONE
            }

            override fun onResponse(call: Call<TV>, response: Response<TV>) {
                progressBar.visibility = View.GONE
                val tv = response.body() as TV
                tvViewModel.setTV(tv)
            }
        })
    }

    private fun showMovie(movie: TV) {
        layout_movie.visibility = View.VISIBLE

        tv_title.text = movie.title
        tv_description.text = movie.overview
        tv_release.text = movie.releaseDate
        tv_vote.text = movie.voteAverage.toString()
        tv_runtime.text = movie.runtime[0].toString()
        Glide.with(this@DetailTVActivity).load("https://image.tmdb.org/t/p/w185" + movie.poster)
            .into(iv_poster)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
