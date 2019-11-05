package id.kido1611.dicoding.moviecataloguefavorite.activity.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.kido1611.dicoding.moviecataloguefavorite.R
import id.kido1611.dicoding.moviecataloguefavorite.adapter.TVAdapter
import id.kido1611.dicoding.moviecataloguefavorite.model.TV
import id.kido1611.dicoding.moviecataloguefavorite.viewmodel.TVListViewModel
import kotlinx.android.synthetic.main.fragment_movie.*


/**
 * A simple [Fragment] subclass.
 */
class TVFragment : Fragment() {

    private lateinit var adapter: TVAdapter
    private lateinit var movieListViewModel: TVListViewModel

    private val movieList = ArrayList<TV>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = TVAdapter(movieList)
        rv_list.adapter = adapter
        rv_list.layoutManager = LinearLayoutManager(context)
        rv_list.setHasFixedSize(true)

        movieListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(TVListViewModel::class.java)
        movieListViewModel.getData().observe(viewLifecycleOwner, Observer{
            movieList.clear()
            movieList.addAll(it)
            adapter.notifyDataSetChanged()
        })

        movieListViewModel.setData(context!!)
    }


}
