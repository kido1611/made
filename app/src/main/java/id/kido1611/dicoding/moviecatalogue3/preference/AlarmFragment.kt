package id.kido1611.dicoding.moviecatalogue3.preference

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.notification.DailyNotification
import id.kido1611.dicoding.moviecatalogue3.notification.ReleaseNotification

class AlarmFragment: PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    Preference.OnPreferenceChangeListener{

    private lateinit var checkBoxDaily: CheckBoxPreference
    private lateinit var checkBoxRelease: CheckBoxPreference

    private var dailyNotification = DailyNotification()
    private var releaseNotification = ReleaseNotification()

    companion object{
        const val DAILY_KEY = "alarm_daily_preference"
        const val RELEASE_KEY = "alarm_release_preference"
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.alarm_preference)

        init()
        setSummary()
    }

    private fun init(){
        checkBoxDaily = findPreference<CheckBoxPreference>(DAILY_KEY) as CheckBoxPreference
        checkBoxRelease = findPreference<CheckBoxPreference>(RELEASE_KEY) as CheckBoxPreference

        checkBoxDaily.onPreferenceChangeListener = this
        checkBoxRelease.onPreferenceChangeListener = this
    }

    private fun setSummary(){
        val sh = preferenceManager.sharedPreferences

        checkBoxDaily.isChecked = sh.getBoolean(DAILY_KEY, false)
        checkBoxRelease.isChecked = sh.getBoolean(RELEASE_KEY, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if(key == DAILY_KEY){
            checkBoxDaily.isChecked = sharedPreferences.getBoolean(DAILY_KEY, false)
        }
        else if(key == RELEASE_KEY){
            checkBoxRelease.isChecked = sharedPreferences.getBoolean(RELEASE_KEY, false)
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        val key = preference.key
        val value = newValue as Boolean
        when(key){
            DAILY_KEY -> {
                if(value){
                    dailyNotification.setRepeatingAlarm(context!!)
                }
                else{
                    dailyNotification.cancelAlarm(context!!)
                }
            }
            RELEASE_KEY -> {
                if(value){
                    releaseNotification.setRepeatingAlarm(context!!)
                }
                else{
                    releaseNotification.cancelAlarm(context!!)
                }
            }
        }

        return true
    }
}