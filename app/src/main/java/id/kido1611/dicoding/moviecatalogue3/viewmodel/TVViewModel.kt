package id.kido1611.dicoding.moviecatalogue3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.kido1611.dicoding.moviecatalogue3.helper.ViewModelHelpers
import id.kido1611.dicoding.moviecatalogue3.model.TV
import id.kido1611.dicoding.moviecatalogue3.network.RetrofitBuilder
import id.kido1611.dicoding.moviecatalogue3.network.TheMovieDBServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVViewModel : ViewModel() {
    private val tv = MutableLiveData<TV>()
    private var viewModelHelpers: ViewModelHelpers? = null

    internal fun setViewModelHelpers(helper: ViewModelHelpers) {
        viewModelHelpers = helper
    }

    internal fun setTV(tvId: Int, language: String) {
        val service = RetrofitBuilder.createService(TheMovieDBServices::class.java)
        val call = service.GetTV(tvId, language)
        call.enqueue(object : Callback<TV> {
            override fun onFailure(call: Call<TV>, t: Throwable) {
                viewModelHelpers?.onFailure(t.message.toString())
            }

            override fun onResponse(call: Call<TV>, response: Response<TV>) {
                val item = response.body() as TV
                tv.postValue(item)
                viewModelHelpers?.onSuccess()
            }

        })
    }

    internal fun getTV(): LiveData<TV> {
        return tv
    }
}