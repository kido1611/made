package id.kido1611.dicoding.moviecatalogue3.preference

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import id.kido1611.dicoding.moviecatalogue3.R

class PreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_holder, AlarmFragment())
            .commit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
