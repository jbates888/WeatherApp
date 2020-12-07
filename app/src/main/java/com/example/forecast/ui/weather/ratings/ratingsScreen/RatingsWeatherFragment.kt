package com.example.forecast.ui.weather.ratings.ratingsScreen

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.R
import kotlinx.coroutines.launch
import com.example.forecast.internal.glide.GlideApp
import com.example.forecast.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.ratings_weather_fragment.*
import org.kodein.di.generic.instance
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

/*
 * Ratings fragment
 */
class RatingsWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: RatingsWeatherViewModelFactory by instance()
    private lateinit var viewModel: RatingsWeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ratings_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RatingsWeatherViewModel::class.java)
        bindUI()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() = launch {
        //get the current weather and location
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        //get the current location
        weatherLocation.observe(this@RatingsWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            //update the location
            updateLocation(location.name)
        })

        //get the current weather
        currentWeather.observe(this@RatingsWeatherFragment, Observer {
            if (it == null) return@Observer
            //update all the ratings
            updateRunningRating(it.temperature, it.precip, it.windSpeed)
            updateBikingRating(it.temperature, it.precip, it.windSpeed)
            updateHikingRating(it.temperature, it.precip, it.windSpeed)
            updateGolfingRating(it.temperature, it.precip, it.windSpeed)
            updateSkiingRating(it.temperature, it.precip, it.windSpeed)
            updateCampingRating(it.temperature, it.precip, it.windSpeed)
            updateSwimmingRating(it.temperature, it.precip, it.windSpeed)
        })
    }

    //update the rating for running on screen
    private fun updateRunningRating(temp: Double, precip: Double, wind: Double){
        var runRating = 0
        if(temp > 9 && temp <= 35){
            runRating += 50;
        }
        if(precip == 0.0){
            runRating += 25;
        } else if(precip <= 2.0){
            runRating += 10;
        }
        if(wind < 3){
            runRating += 35;
        } else if(wind < 10){
            runRating += 15;
        }

        RunProgressBar.max = 100;
        RunProgressBar.progress = runRating
    }
    //update the rating for biking on screen
    private fun updateBikingRating(temp: Double, precip: Double, wind: Double){
        var bikeRating = 0
        if(temp > 9 && temp <= 35){
            bikeRating += 50;
        }
        if(precip == 0.0){
            bikeRating += 25;
        } else if(precip <= 5.0){
            bikeRating += 10;
        }
        if(wind < 1){
            bikeRating += 25;
        } else if(wind < 8){
            bikeRating += 15;
        }

        BikeProgressBar.max = 100;
        BikeProgressBar.progress = bikeRating
    }
    //update the rating for hiking on screen
    private fun updateHikingRating(temp: Double, precip: Double, wind: Double){
        var hikeRating = 0
        if(temp > 10 && temp <= 25){
            hikeRating += 50;
        }
        if(precip == 0.0){
            hikeRating += 25;
        } else if(precip <= 5){
            hikeRating += 15;
        }
        if(wind < 5){
            hikeRating += 25;
        } else if(wind < 10){
            hikeRating += 15;
        }

        HikeProgressBar.max = 100;
        HikeProgressBar.progress = hikeRating
    }
    //update the rating for golfing on screen
    private fun updateGolfingRating(temp: Double, precip: Double, wind: Double){
        var golfRating = 0
        if(temp > 11 && temp <= 30){
            golfRating += 50;
        }
        if(precip <= 3.0){
            golfRating += 10;
        } else if(precip == 0.0){
            golfRating += 35;
        }
        if(wind == 0.0){
            golfRating += 25;
        } else if(wind < 5){
            golfRating += 10;
        }

        GolfProgressBar.max = 100;
        GolfProgressBar.progress = golfRating
    }
    //update the rating for skiing on screen
    private fun updateSkiingRating(temp: Double, precip: Double, wind: Double){
        var skiRating = 0
        if(temp > -25 && temp <= 0){
            skiRating += 50;
        }
        if(precip > 5.0){
            skiRating += 25;
        } else if(precip > 0.0){
            skiRating += 15;
        }
        if(wind < 5){
            skiRating += 25;
        }
        if(temp > 2){
            skiRating = 0;
        }

        SkiProgressBar.max = 100;
        SkiProgressBar.progress = skiRating
    }
    //update the rating for camping on screen
    private fun updateCampingRating(temp: Double, precip: Double, wind: Double){
        var campRating = 0
        if(temp > 9 && temp <= 35){
            campRating += 50;
        }
        if(precip == 0.0){
            campRating += 25;
        } else if(precip <= 3.0){
            campRating += 10;
        }
        if(wind < 10){
            campRating += 25;
        }

        CampProgressBar.max = 100;
        CampProgressBar.progress = campRating
    }
    //update the rating for swimming on screen
    private fun updateSwimmingRating(temp: Double, precip: Double, wind: Double){
        var swimRating = 0
        if(temp > 23 && temp <= 38){
            swimRating += 50;
        }
        if(precip == 0.0){
            swimRating += 25;
        }
        if(wind < 10){
            swimRating += 25;
        }
        if(precip >= 3.0 || temp < 10){
            swimRating = 0;
        }

        SwimProgressBar.max = 100;
        SwimProgressBar.progress = swimRating
    }
    //update the location
    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

}
