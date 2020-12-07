package com.example.forecast.ui.weather.current

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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

/*
 * The apps main fragment, shows the current weather
 */
class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //set the view model
        viewModel = ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        //bind the elements
        bindUI()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        //update the current location
        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })
        //update the current weather
        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer
            //Set call the methods to update the weather on screen
            updateTemperatures(it.temperature, it.feelslike)
            updateCondition(it.weatherDescriptions[0])
            updatePrecipitation(it.precip)
            updateWind(it.windDir, it.windSpeed)
            updateVisibility(it.visibility)

            //set the background color based on the current weather
            if (it.weatherDescriptions[0] == "Sunny") {
                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#E1F5FE"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.parseColor(
                            "#673AB7"
                        )
                    )
                )
            } else if (it.weatherDescriptions[0] == "Overcast" || it.weatherDescriptions[0] == "Cloudy") {
                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#a7cad1"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.parseColor(
                            "#673AB7"
                        )
                    )
                )
            } else if (it.weatherDescriptions[0] == "Partly cloudy") {
                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#E1F5FE"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.parseColor(
                            "#673AB7"
                        )
                    )
                )
            } else if (it.weatherDescriptions[0] == "Clear") {
                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#673AB7"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.parseColor(
                            "#673AB7"
                        )
                    )
                )
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
                it.weatherDescriptions[0] == "Moderate or heavy rain in area with thunder"
            ) {
                this@CurrentWeatherFragment.view?.setBackgroundColor(Color.parseColor("#E1F5FE"))
                (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.parseColor(
                            "#673AB7"
                        )
                    )
                )
            }

            //load the current weather image
            GlideApp.with(this@CurrentWeatherFragment).load(it.weatherIcons[0]).into(condition_image)
        })
    }
    //set the unit system
    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    //update the viability speed on screen
    private fun updateVisibility(visibilityDistance: Double) {
        //get the selected unit
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        if (unitAbbreviation == "km") {
            val metricVis = "Visibility: $visibilityDistance $unitAbbreviation"
            visibilityText.text = metricVis
        } else if (unitAbbreviation == "mi.") {
            val impVisNum = visibilityDistance / 1.609
            val imperialVis = "Visibility: ${decimalFormat.format(impVisNum)} $unitAbbreviation"
            visibilityText.text = imperialVis
        }
    }

    //update the screens condition
    private fun updateCondition(condition: String) {
        conditionText.text = condition
    }

    //update the temp on the screen with proper unit system
    private fun updateTemperatures(temperature: Double, feelsLikeTemp: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if (unitAbbreviation == "°C") {
            val metricText = "$temperature$unitAbbreviation"
            textView_temperature.text = metricText
            val metricFeelsLike = "Feels Like $feelsLikeTemp$unitAbbreviation"
            feels_like_text.text = metricFeelsLike
        } else if (unitAbbreviation == "°F") {
            //convert to Fahrenheit
            val tempInImperial = (temperature * 9 / 5) + 32
            val imperialText = "${decimalFormat.format(tempInImperial)}$unitAbbreviation"
            textView_temperature.text = imperialText
            val imperialFeels = "Feels Like ${(feelsLikeTemp * 9 / 5) + 32}$unitAbbreviation"
            feels_like_text.text = imperialFeels
        }
    }

    //update the screens precipitation
    private fun updatePrecipitation(precipitationVolume: Double) {
        //get the selected unit
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        if (unitAbbreviation == "mm") {
            val metricPrecip = "Precipitation: $precipitationVolume $unitAbbreviation"
            precipitationText.text = metricPrecip
        } else if (unitAbbreviation == "in") {
            val impPrecip = precipitationVolume / 25.4
            val imperialPrecipText = "Precipitation: ${decimalFormat.format(impPrecip)} $unitAbbreviation"
            precipitationText.text = imperialPrecipText
        }
    }

    //update the wind speed on screen
    private fun updateWind(windDirection: String, windSpeed: Double) {
        //get the selected unit
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        if (unitAbbreviation == "kph") {
            val metricWind = "Wind: $windDirection, $windSpeed $unitAbbreviation"
            windText.text = metricWind
        } else if (unitAbbreviation == "mph") {
            //convert to MPH
            val windSpeedImperial = windSpeed / 1.609
            val imperialWind = "Wind: $windDirection, ${decimalFormat.format(windSpeedImperial)} $unitAbbreviation"
            windText.text = imperialWind
        }
    }

    //update the title to the current location
    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Weather for: $location"
    }

    companion object {
        val decimalFormat = DecimalFormat("0.00")
    }
}
