package id.kido1611.dicoding.moviecatalogue3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.kido1611.dicoding.moviecatalogue3.db.TVDAO
import id.kido1611.dicoding.moviecatalogue3.helper.ViewModelHelpers
import id.kido1611.dicoding.moviecatalogue3.model.TV
import id.kido1611.dicoding.moviecatalogue3.model.TVResponse
import id.kido1611.dicoding.moviecatalogue3.network.RetrofitBuilder
import id.kido1611.dicoding.moviecatalogue3.network.TheMovieDBServices
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVListViewModel : ViewModel() {
    private val listTV = MutableLiveData<ArrayList<TV>>()
    private var viewModelHelpers: ViewModelHelpers? = null

    internal fun setViewModelHelpers(helper: ViewModelHelpers) {
        viewModelHelpers = helper
    }

    internal fun setTV() {
        val service = RetrofitBuilder.createService(TheMovieDBServices::class.java)
        val call = service.DiscoverTV("en-US")
        call.enqueue(object : Callback<TVResponse> {
            override fun onFailure(call: Call<TVResponse>, t: Throwable) {
                viewModelHelpers?.onFailure(t.message.toString())
            }

            override fun onResponse(call: Call<TVResponse>, response: Response<TVResponse>) {
                val tvList = response.body() as TVResponse
                listTV.postValue(tvList.tvLists as ArrayList<TV>)

                viewModelHelpers?.onSuccess()
            }
        })
    }

    internal fun setFavoriteTV(tvDAP: TVDAO) {
        GlobalScope.launch {
            val list = tvDAP.getAllTV() as ArrayList<TV>
            listTV.postValue(list)
        }
    }

    internal fun getTV(): LiveData<ArrayList<TV>> {
        return listTV
    }
}