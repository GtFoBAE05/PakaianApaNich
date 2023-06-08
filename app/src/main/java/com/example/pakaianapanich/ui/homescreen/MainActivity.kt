package com.example.pakaianapanich.ui.homescreen

import android.Manifest
import android.content.ContentValues.TAG
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pakaianapanich.data.source.remote.response.WeatherResponse
import com.example.pakaianapanich.databinding.ActivityMainBinding
import com.example.pakaianapanich.utils.CurrentDate
import com.example.pakaianapanich.utils.Resource
import com.example.pakaianapanich.utils.getOutfitImage
import com.example.pakaianapanich.utils.getWeatherIcon
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding


    private val mapsFinePerms = Manifest.permission.ACCESS_FINE_LOCATION
    private val mapsCoarsePerms = Manifest.permission.ACCESS_COARSE_LOCATION

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lat: String = ""
    private var lon: String = ""

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasMapsFinePerms()) {
            requestMapsFinePerms()
        }

        if (!hasMapsCoarsePerms()) {
            requestMapsCoarsePerms()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestLocationUpdates()

        setupObserver()


    }


    private fun setupObserver() {
        homeViewModel.weatherCondition.observe(this) {
            when (it) {
                is Resource.Success -> {
                    setupView(it.data)

                }

                is Resource.Error -> {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

                }

                is Resource.Loading -> {
                }
            }
        }
    }

    private fun setupView(weather: WeatherResponse) {
        binding.locationTv.text = weather.name

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.dateTv.text = CurrentDate()
        }
        binding.tempTv.text = weather.main?.temp.toString()

        binding.weatherTv.text = weather.weather.first().description

        val iconUrl = getWeatherIcon(weather.weather.first().icon.toString())
        Glide.with(this).load(iconUrl).into(binding.weatherImageView)

        binding.windHumVisCard.windNumberTv.text = "${weather.wind?.speed} m/s"
        binding.windHumVisCard.humidityNumberTv.text = "${weather.main?.humidity}%"
        binding.windHumVisCard.visibilityNumberTv.text = "${weather.visibility} m"


        val outfitUrl = getOutfitImage(weather.weather.first().main.toString())
        Glide.with(this).load(outfitUrl).into(binding.outfitImageView)


    }

    private fun getLastLocation() {
        if (hasMapsFinePerms() && hasMapsCoarsePerms()) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                lat = it.latitude.toString()
                lon = it.longitude.toString()
                geocoder(it.latitude, it.longitude)
                Log.e(TAG, "getLastLocation: lat: $lat / lon:$lon / time: ${it.time}")

                homeViewModel.getWeather(lat, lon)

            }


        } else {
            Toast.makeText(
                this,
                "Tidak ada akses lokasi",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createLocationRequest() = LocationRequest.create().apply {
        interval = TimeUnit.MINUTES.toMillis(1)
        fastestInterval = TimeUnit.MINUTES.toMillis(1)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun createLocationCallback() = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                getLastLocation()
            }
        }
    }


    private fun requestLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                createLocationRequest(),
                createLocationCallback(), Looper.myLooper()
            )
        } catch (ex: SecurityException) {
            Log.e(TAG, "Lost location permission. Could not request updates. $ex")
        }
    }


    private fun geocoder(lat: Double, lon: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val cityname = address.subAdminArea
//                binding.locationTv.text = cityname
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun hasMapsFinePerms() =
        EasyPermissions.hasPermissions(
            this,
            mapsFinePerms
        )

    private fun hasMapsCoarsePerms() =
        EasyPermissions.hasPermissions(
            this,
            mapsCoarsePerms
        )

    private fun requestMapsFinePerms() {
        EasyPermissions.requestPermissions(
            this,
            "Membutuhkan Akses Maps",
            1,
            mapsFinePerms,
        )
    }


    private fun requestMapsCoarsePerms() {
        EasyPermissions.requestPermissions(
            this,
            "Membutuhkan Akses Maps",
            2,
            mapsCoarsePerms,
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            if (requestCode == 1) {
                requestMapsFinePerms()
            }

            if (requestCode == 2) {
                requestMapsCoarsePerms()
            }
        }
    }
}