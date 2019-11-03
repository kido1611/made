package id.kido1611.dicoding.moviecatalogue3.activity.main.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.adapter.TVAdapter
import id.kido1611.dicoding.moviecatalogue3.db.MovieDatabase
import id.kido1611.dicoding.moviecatalogue3.helper.ViewModelHelpers
import id.kido1611.dicoding.moviecatalogue3.model.TV
import id.kido1611.dicoding.moviecatalogue3.viewmodel.TVListViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class TVFragment : Fragment(), ViewModelHelpers {

    private lateinit var adapter: TVAdapter
    private lateinit var tvListViewModel: TVListViewModel
    private lateinit var movieDatabase: MovieDatabase

    private var tvList = ArrayList<TV>()
    private var successLoad = false
    private var errorMessage = ""

    private var isFavorite = false

    companion object {
        const val MOVIES_STATE = "movie"
        const val SUCCESS_STATE = "success"
        const val ERROR_MESSAGE_STATE = "error_message"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null) {
            isFavorite = arguments!!.getBoolean("favorite", false)
        }
        adapter = TVAdapter(tvList)
        rv_list.adapter = adapter
        rv_list.layoutManager = LinearLayoutManager(activity)

        movieDatabase = MovieDatabase.getInstance(context!!.applicationContext) as MovieDatabase

        tvListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(TVListViewModel::class.java)
        tvListViewModel.setViewModelHelpers(this)
        tvListViewModel.getTV().observe(viewLifecycleOwner, Observer {
            if (it != null) {

                tvList.clear()
                tvList.addAll(it)
                adapter.notifyDataSetChanged()

                showList(true)
                showError(false, "")
                showLoading(false)
            }
        })

        if (savedInstanceState == null) {
            loadData()
        } else {
            errorMessage = savedInstanceState.getString(ERROR_MESSAGE_STATE)
            successLoad = savedInstanceState.getBoolean(SUCCESS_STATE)

            if (successLoad) {
                tvList.clear()
                tvList.addAll(savedInstanceState.getParcelableArrayList(MOVIES_STATE))
                adapter.notifyDataSetChanged()
            } else {
                showLoading(false)
                showList(false)
                showError(!successLoad, errorMessage)
            }
        }

        btn_reload.setOnClickListener {
            loadData()
        }

        if (isFavorite) {
            setHasOptionsMenu(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.movie_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_reload -> {
                loadData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(MOVIES_STATE, tvList)
        outState.putBoolean(SUCCESS_STATE, successLoad)
        outState.putString(ERROR_MESSAGE_STATE, errorMessage)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showList(state: Boolean) {
        if (state) {
            rv_list.visibility = View.VISIBLE
        } else {
            rv_list.visibility = View.GONE
        }
    }

    private fun showError(state: Boolean, message: String) {
        if (state) {
            layout_message.visibility = View.VISIBLE
        } else {
            layout_message.visibility = View.GONE
        }
        tv_message.text = message
    }

    private fun loadData() {
        showLoading(true)
        showList(false)
        showError(false, "")

        if (isFavorite) {
            tvListViewModel.setFavoriteTV(movieDatabase.tvDAO())
        } else {
            tvListViewModel.setTV()
        }
    }

    override fun onFailure(message: String) {
        showLoading(false)
        showList(false)
        showError(true, message)

        successLoad = false
        errorMessage = message
    }

    override fun onSuccess() {
        successLoad = true
    }


}
