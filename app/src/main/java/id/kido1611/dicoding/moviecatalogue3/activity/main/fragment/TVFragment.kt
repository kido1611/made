package id.kido1611.dicoding.moviecatalogue3.activity.main.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.viewmodel.TVListViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class TVFragment : Fragment() {

    private lateinit var adapter: TVAdapter
    private lateinit var tvListViewModel: TVListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = TVAdapter()

        rv_list.adapter = adapter
        rv_list.layoutManager = LinearLayoutManager(activity)

        tvListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(TVListViewModel::class.java)
        tvListViewModel.getTV().observe(this, Observer {
            if (it != null) {
                adapter.setData(it)
                showLoading(false)
            }
        })

        loadData()
    }

    private fun showLoading(state: Boolean) {
        if (progressBar != null) {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadData() {
        showLoading(true)
        tvListViewModel.setTV()
    }
}
