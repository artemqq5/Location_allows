package com.example.allows

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var enabled: LocationManager
    var state = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initL()
        enabled = this.getSystemService(LOCATION_SERVICE) as LocationManager
        state = enabled.isProviderEnabled(LocationManager.GPS_PROVIDER)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        findViewById<Button>(R.id.button).setOnClickListener {
            getAllows()
        }

    }



    private fun getAllows() {

        Log.i("cgghgfdr", "getAllows()")

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) -> {

                Log.i("cgghgfdr", "COARSE")
                if (enabled.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.i("cgghgfdr", "enabled.is")
                    getLocation()

                } else {
                    Log.i("cgghgfdr", "not on gps")
                    startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
                }

            }
            else -> {
                Log.i("cgghgfdr", "else")
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }

    private fun initL() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isDane ->

                if (isDane) {
                    // Пользователь предоставил разрешение
                    getLocation()
                    Log.i("ghgbf37fh", "Пользователь предоставил разрешение")

                } else {
                    getAllows()
                    Log.i("ghgbf37fh", "Пользователь НЕ предоставил разрешение")
                } // Пользователь НЕ предоставил разрешение
            }
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                val locationRequest = location?.run {
                    val geo = Geocoder(applicationContext, Locale.getDefault())

                    geo.getFromLocation(this.latitude, this.longitude, 1)[0].locality
                } ?: "null"

                findViewById<TextView>(R.id.text).text = locationRequest

            }
    }

}
