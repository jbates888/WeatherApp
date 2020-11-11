package com.example.forecast.ui.weather.current

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.R
import com.example.forecast.internal.glide.GlideApp
import com.example.forecast.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.DecimalFormat


class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        bindUI()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if (location == null) return@Observer

            Log.d("location.name", location.name)
            Log.d("location.timezoneId", location.timezoneId)
            Log.d("location.country", location.country)
            Log.d("location.localtime", location.localtime)
            Log.d("location.region", location.region)
            Log.d("location.utcOffset", location.utcOffset)
            Log.d("location.id", "${location.id}")
            Log.d("location.lat", "${location.lat}")
            Log.d("location.localtimeEpoch", "${location.localtimeEpoch}")
            Log.d("location.lon", "${location.lon}")
            Log.d("location.zonedDateTime", "${location.zonedDateTime}")

            updateLocation(location.name)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE
            updateDateToToday()

            updateTemperatures(it.temperature, it.feelslike)
            updateCondition(it.weatherDescriptions[0])
            updatePrecipitation(it.precip)
            updateWind(it.windDir, it.windSpeed)
            updateVisibility(it.visibility)

            if (it.weatherDescriptions[0] == "Sunny") {
                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#E1F5FE"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#673AB7")))
            } else if (it.weatherDescriptions[0] == "Overcast" || it.weatherDescriptions[0] == "Cloudy" ) {
                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#BDBDBD"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#673AB7")))
            } else if (it.weatherDescriptions[0] == "Partly cloudy") {
                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#E1F5FE"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#673AB7")))
            } else if (it.weatherDescriptions[0] == "Clear") {
                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#673AB7"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#673AB7")))
            } else if (it.weatherDescriptions[0] == "Patchy rain nearby" ||
                    it.weatherDescriptions[0] == "Patchy freezing drizzle nearby" ||
                    it.weatherDescriptions[0] == "Thundery outbreaks in nearby" ||
                    it.weatherDescriptions[0] == "Light drizzle" ||
                    it.weatherDescriptions[0] == "Freezing drizzle" ||
                    it.weatherDescriptions[0] == "Heavy freezing drizzle" ||
                    it.weatherDescriptions[0] == "Patchy light rain" ||
                    it.weatherDescriptions[0] == "Moderate rain at times" ||
                    it.weatherDescriptions[0] == "Moderate rain" ||
                    it.weatherDescriptions[0] == "Heavy rain at times" ||
                    it.weatherDescriptions[0] == "Light freezing rain" ||
                    it.weatherDescriptions[0] == "Light rain shower" ||
                    it.weatherDescriptions[0] == "Moderate or heavy rain shower" ||
                    it.weatherDescriptions[0] == "Torrential rain shower" ||
                    it.weatherDescriptions[0] == "Patchy light rain in area with thunder" ||
                    it.weatherDescriptions[0] == "Moderate or heavy rain in area with thunder") {

                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#E1F5FE"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#673AB7")))
            }

            GlideApp.with(this@CurrentWeatherFragment)
                .load(it.weatherIcons[0])
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Current Weather"
    }

    private fun updateTemperatures(temperature: Double, feelsLikeTemp: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if (unitAbbreviation == "°C") {
            val metricTemperatureText = "$temperature$unitAbbreviation"
            textView_temperature.text = metricTemperatureText
            val metricFeelsLikeTemperatureText = "Feels Like $feelsLikeTemp$unitAbbreviation"
            textView_feels_like_temperature.text = metricFeelsLikeTemperatureText
        } else if (unitAbbreviation == "°F") {
            val tempInImperial = (temperature * 9 / 5) + 32
            val imperialTemperatureText = "${decimalFormat.format(tempInImperial)}$unitAbbreviation"
            textView_temperature.text = imperialTemperatureText
            val imperialFeelsLikeTemperatureText =
                "Feels Like ${(feelsLikeTemp * 9 / 5) + 32}$unitAbbreviation"
            textView_feels_like_temperature.text = imperialFeelsLikeTemperatureText
        }
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        if (unitAbbreviation == "mm") {
            val metricPrecipitationText = "Precipitation: $precipitationVolume $unitAbbreviation"
            textView_precipitation.text = metricPrecipitationText
        } else if (unitAbbreviation == "in") {
            val precipitationVolumeImperial = precipitationVolume / 25.4
            val imperialPrecipitationText =
                "Precipitation: ${decimalFormat.format(precipitationVolumeImperial)} $unitAbbreviation"
            textView_precipitation.text = imperialPrecipitationText
        }
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        if (unitAbbreviation == "kph") {
            val metricWindText = "Wind: $windDirection, $windSpeed $unitAbbreviation"
            textView_wind.text = metricWindText
        } else if (unitAbbreviation == "mph") {
            val windSpeedImperial = windSpeed / 1.609
            val imperialWindText =
                "Wind: $windDirection, ${decimalFormat.format(windSpeedImperial)} $unitAbbreviation"
            textView_wind.text = imperialWindText
        }
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        if (unitAbbreviation == "km") {
            val metricVisibilityText = "Visibility: $visibilityDistance $unitAbbreviation"
            textView_visibility.text = metricVisibilityText
        } else if (unitAbbreviation == "mi.") {
            val visibilityDistanceImperial = visibilityDistance / 1.609
            val imperialVisibilityText =
                "Visibility: ${decimalFormat.format(visibilityDistanceImperial)} $unitAbbreviation"
            textView_visibility.text = imperialVisibilityText
        }
    }

    companion object {
        val decimalFormat = DecimalFormat("0.00")
    }
}
