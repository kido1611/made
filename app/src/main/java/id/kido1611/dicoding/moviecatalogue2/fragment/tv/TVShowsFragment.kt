package id.kido1611.dicoding.moviecatalogue2.fragment.tv


import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import id.kido1611.dicoding.moviecatalogue2.R
import id.kido1611.dicoding.moviecatalogue2.model.TV
import kotlinx.android.synthetic.main.fragment_tvshows.*

/**
 * A simple [Fragment] subclass.
 */
class TVShowsFragment : Fragment() {

    private lateinit var adapter: TVAdapter
    private var tvLists = arrayListOf<TV>()

    private lateinit var dataTitle: Array<String>
    private lateinit var dataVote: Array<String>
    private lateinit var dataDescription: Array<String>
    private lateinit var dataPoster: TypedArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TVAdapter(tvLists)

        rv_list.adapter = adapter
        rv_list.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.grid_count))

        prepare()
        addItem()
    }

    private fun prepare() {
        dataTitle = resources.getStringArray(R.array.tv_title)
        dataDescription = resources.getStringArray(R.array.tv_description)
        dataVote = resources.getStringArray(R.array.tv_vote)
        dataPoster = resources.obtainTypedArray(R.array.tv_poster)
    }

    private fun addItem() {
        for (position in dataTitle.indices) {
            val tv = TV(
                dataTitle[position],
                dataDescription[position],
                dataVote[position].toFloat(),
                dataPoster.getResourceId(position, -1)
            )
            tvLists.add(tv)
        }

        adapter.notifyDataSetChanged()
    }


}
