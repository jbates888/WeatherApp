package com.example.appwidget
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.RemoteViews
import com.example.forecast.R
import com.example.forecast.foreground.MainActivity


/**
 * Implementation of App Widget functionality.
 */
class WeatherWidget : AppWidgetProvider(), LocationListener {

    @SuppressLint("MissingPermission")
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        /** PendingIntent to launch the MainActivity when the widget was clicked  */
        val views = RemoteViews(context.packageName, R.layout.weather_widget)
       // val icon: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.search_bar)
        views.setImageViewResource(R.id.imageView2, R.drawable.search_bar)
        val launchMain = Intent(context, MainActivity::class.java)
        launchMain.putExtra("Settings", true)

        val pendingMainIntent = PendingIntent.getActivity(context, 0, launchMain, 0)
        views.setOnClickPendingIntent(R.id.imageView2, pendingMainIntent)
        appWidgetIds.forEach { appWidgetId ->
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onLocationChanged(location: Location) {

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }
}








