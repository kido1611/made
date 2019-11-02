package id.kido1611.dicoding.moviecatalogue3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.kido1611.dicoding.moviecatalogue3.model.TV

class TVViewModel : ViewModel() {
    private val tv = MutableLiveData<TV>()

    internal fun setTV(item: TV) {
        tv.postValue(item)
    }

    internal fun getTV(): LiveData<TV> {
        return tv
    }
}