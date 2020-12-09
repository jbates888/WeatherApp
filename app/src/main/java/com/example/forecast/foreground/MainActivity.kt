package com.example.forecast.foreground

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.example.forecast.R
import com.example.forecast.R.*
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

/*
 * Main activity
 */
class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private lateinit var navController: NavController
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private val locationCallback = object : LocationCallback(){

    }

    //When activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)
        //set up intent to go to settings screen
        val settings : Boolean = intent.getBooleanExtra("Settings", false)
        //set up the nav controller for navigating fragments
        navController = Navigation.findNavController(this, R.id.nav_fragment)
        if(settings) navController.navigate(R.id.settingsFragment)
        //set bottom nav menu
        nav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
        //request the user to use their current location
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_ACCESS_LOCATION)
        if (hasLocationPermission()) {
            LifecycleBoundLocationManager(this, fusedLocationProviderClient, locationCallback)
        }
    }

    // when location is requested form the user
    override fun onRequestPermissionsResult(request: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (request == MY_PERMISSION_ACCESS_LOCATION) {
            //if the user said yes, set the lifecycle
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                LifecycleBoundLocationManager(this, fusedLocationProviderClient, locationCallback)
            }
            //if the user says no, tell them to turn on permission in the settings
            else {
                Toast.makeText(this, "In settings, allow this app to use your location", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
    companion object {
        const val MY_PERMISSION_ACCESS_LOCATION = 12
    }

    //check if user has given permission for location services
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


}
