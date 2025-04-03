package com.mustafa.weathernow.alert.recever

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.mustafa.weathernow.R
import com.mustafa.weathernow.data.location.repo.LocationRepository
import com.mustafa.weathernow.data.location.sources.local.LocationDatabase
import com.mustafa.weathernow.data.location.sources.local.LocationLocalDatasource
import com.mustafa.weathernow.data.location.sources.remote.LocationRemoteDataSource
import com.mustafa.weathernow.data.location.sources.remote.LocationRetrofitHelper
import com.mustafa.weathernow.data.location.sources.shared_prefs.LocationSharedPrefs
import com.mustafa.weathernow.data.weather.repos.WeatherRepository
import com.mustafa.weathernow.data.weather.sources.local.WeatherDatabase
import com.mustafa.weathernow.data.weather.sources.local.WeatherLocalDatasourceImpl
import com.mustafa.weathernow.data.weather.sources.remote.RetrofitHelper
import com.mustafa.weathernow.data.weather.sources.remote.WeatherRemoteDatasourceImpl
import com.mustafa.weathernow.main_screen.view.MainActivity
import com.mustafa.weathernow.utils.AlarmBroadcastReceiverConstants
import com.mustafa.weathernow.utils.GeoCoderHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "Alerts"
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var locationRepository: LocationRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AlarmBroadcastReceiverConstants.ALARM_ACTION) {
            val alarmId = intent.getIntExtra(AlarmBroadcastReceiverConstants.ALARM_ID, -1)
            val longitude = intent.getDoubleExtra(AlarmBroadcastReceiverConstants.LONGITUDE, 0.0)
            val latitude = intent.getDoubleExtra(AlarmBroadcastReceiverConstants.LATITUDE, 0.0)

            initiateRepositories(context)

            pushNotification(context, longitude, latitude, alarmId)
            deleteAlarm(alarmId)
        }

    }

    private fun initiateRepositories(context: Context) {
        weatherRepository = WeatherRepository.getInstance(
            WeatherLocalDatasourceImpl(WeatherDatabase.getInstance(context).getWeatherDao()),
            WeatherRemoteDatasourceImpl(RetrofitHelper.retrofitService)
        )
        locationRepository = LocationRepository.getInstance(
            LocationRemoteDataSource(LocationRetrofitHelper.retrofitService),
            LocationLocalDatasource(LocationDatabase.getInstance(context).getLocatingDao()),
            LocationSharedPrefs(context)
        )
    }

    private fun deleteAlarm(alarmId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            locationRepository.deleteAlert(alarmId)
        }
    }

    private fun pushNotification(
        context: Context,
        longitude: Double,
        latitude: Double,
        alarmId: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                weatherRepository
                    .getAllWeatherData(longitude, latitude)
                    .catch { }
                    .collect {
                        createNotificationChannel(context)
                        val soundUri =
                            "android.resource://${context.packageName}/${R.raw.alert_sound}".toUri()

                        val notificationIntent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            putExtra(AlarmBroadcastReceiverConstants.LONGITUDE, longitude)
                            putExtra(AlarmBroadcastReceiverConstants.LATITUDE, latitude)
                        }

                        val pendingIntent = PendingIntent.getActivity(
                            context,
                            alarmId,
                            notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )


                        var notificationBuilder = NotificationCompat
                            .Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.clouds_icon)
                            .setContentTitle(
                                GeoCoderHelper(context).getCityName(it.lat, it.lon)
                                    ?: context.getString(R.string.alert)
                            )
                            .setContentText(
                                it.current?.weather?.first()?.description ?: context.getString(
                                    R.string.click_to_show_weather_condition
                                )
                            )
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setSound(soundUri)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)

                        with(NotificationManagerCompat.from(context)) {
                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return@with
                            }
                            notify(alarmId, notificationBuilder.build())
                        }
                    }
            } catch (ex: Exception) {
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri =
                "android.resource://${context.packageName}/${R.raw.alert_sound}".toUri()

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val name = context.getString(R.string.app_name)
            val descriptionText = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(soundUri, audioAttributes)
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}