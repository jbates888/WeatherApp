package com.example.AppWidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.forecast.R
import com.example.forecast.ui.MainActivity
import com.example.forecast.ui.weather.current.CurrentWeatherViewModel


/**
 * Implementation of App Widget functionality.
 */
class WeatherWidget : AppWidgetProvider() {

    private lateinit var viewModel: CurrentWeatherViewModel
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = "text for widget"
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.weather_widget)

    views.setTextViewText(R.id.widget_temp, widgetText);
    /** PendingIntent to launch the MainActivity when the widget was clicked  */
    val launchMain = Intent(context, MainActivity::class.java)
    val pendingMainIntent = PendingIntent.getActivity(context, 0, launchMain, 0)
    views.setOnClickPendingIntent(R.id.widget_temp, pendingMainIntent)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
