package id.kido1611.dicoding.moviecatalogue3.activity.main.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.adapter.FavoritePagerAdapter
import kotlinx.android.synthetic.main.fragment_favorite_container.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteContainerFragment : Fragment() {

    private lateinit var adapter: FavoritePagerAdapter
    private var fragmentList = ArrayList<Fragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_container, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = FavoritePagerAdapter(childFragmentManager, context!!, fragmentList)
        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)

        val bundle = Bundle()
        bundle.putBoolean("favorite", true)
        val fragment = MovieFragment()
        fragment.arguments = bundle
        fragmentList.add(fragment)

        val fragment2 = TVFragment()
        fragment2.arguments = bundle
        fragmentList.add(fragment2)
        adapter.notifyDataSetChanged()
    }


}
