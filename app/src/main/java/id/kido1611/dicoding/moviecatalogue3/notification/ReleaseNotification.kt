package id.kido1611.dicoding.moviecatalogue3.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import id.kido1611.dicoding.moviecatalogue3.R
import id.kido1611.dicoding.moviecatalogue3.activity.detailmovie.DetailMovieActivity
import id.kido1611.dicoding.moviecatalogue3.activity.main.MainActivity
import id.kido1611.dicoding.moviecatalogue3.model.Movie
import id.kido1611.dicoding.moviecatalogue3.model.MovieResponse
import id.kido1611.dicoding.moviecatalogue3.network.RetrofitBuilder
import id.kido1611.dicoding.moviecatalogue3.network.TheMovieDBServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReleaseNotification : BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_ID = 103
        const val NOTIFICATION_REQUEST_CODE = 104
        const val CHANNEL_ID = "channel_release"
        const val CHANNEL_NAME = "Release Channel"
        const val MAX_NOTIFICATION = 2
        const val RELEASE_CHANNEL_GROUP = "release_group"
    }

    private var movieList = ArrayList<Movie>()
    private var movieNotifList = ArrayList<Movie>()

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        loadData(context)
    }

    private fun loadData(context: Context){
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(date)
        val service = RetrofitBuilder.createService(TheMovieDBServices::class.java)
        val call = service.ReleaseMovie(currentDate, currentDate)
        call.enqueue(object : Callback<MovieResponse>{
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(context, context.resources.getString(R.string.alarm_failed_get_data, t.message.toString()), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val movieResponse = response.body() as MovieResponse
                movieNotifList.clear()
                movieList.clear()
                movieList.addAll(movieResponse.movieLists)

                movieList.forEach {
                    showAlarmNotification(context, it)
                }
            }

        })
    }

    private fun showAlarmNotification(context: Context, movie: Movie) {
        movieNotifList.add(movie)

        val builder : NotificationCompat.Builder

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(movieNotifList.size < MAX_NOTIFICATION){
            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(DetailMovieActivity.MOVIE_ITEM, movie.id)
            val pendingIntent = PendingIntent.getActivity(
                context,
                movie.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                .setContentTitle(context.resources.getString(R.string.app_name))
                .setContentText(context.resources.getString(R.string.alarm_release_new_movie, movie.title))
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setGroup(RELEASE_CHANNEL_GROUP)
                .setGroupSummary(true)
                .setAutoCancel(true)
        }
        else{
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            val pendingIntent = PendingIntent.getActivity(
                context,
                movie.id,
                intent,
                0
            )

            val inboxStyle = NotificationCompat.InboxStyle()
                .setBigContentTitle(context.getString(R.string.alarm_release_message))
                .setSummaryText(context.resources.getString(R.string.alarm_release_summary, movieNotifList.size))
                movieNotifList.forEach {
                    inboxStyle.addLine(context.resources.getString(R.string.alarm_release_new_movie, it.title))
                }

            builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                .setContentTitle(context.resources.getString(R.string.app_name))
                .setContentText(context.resources.getString(R.string.alarm_release_summary, movieNotifList.size))
                .setGroup(RELEASE_CHANNEL_GROUP)
                .setGroupSummary(true)
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }

    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseNotification::class.java)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context,
            NOTIFICATION_ID, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(context, context.getString(R.string.alarm_success_setup), Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseNotification::class.java)
        val requestCode = NOTIFICATION_ID
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, context.getString(R.string.alarm_success_cancel), Toast.LENGTH_SHORT).show()
    }
}
