package id.kido1611.dicoding.moviecatalogue2

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import id.kido1611.dicoding.moviecatalogue2.fragment.movie.MoviesFragment
import id.kido1611.dicoding.moviecatalogue2.fragment.tv.TVShowsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_movies -> {
                    loadMovies()
                }
                R.id.navigation_tvshows -> {
                    loadTVShows()
                }
            }
            true
        }

        loadMovies()
    }

    private fun loadMovies() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                MoviesFragment(), MoviesFragment::class.java.simpleName
            )
            .commit()
    }

    private fun loadTVShows() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                TVShowsFragment(), TVShowsFragment::class.java.simpleName
            )
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_locale_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
