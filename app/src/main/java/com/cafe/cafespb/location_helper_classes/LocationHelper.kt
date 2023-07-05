package com.cafe.cafespb.location_helper_classes

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import java.util.Locale

//класс использ-ся для получ-я локации пользователя
class LocationHelper(private val context: Context) {

    private var locationManager: LocationManager? = null

    fun getLocation(callback: (Location) -> Unit) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                callback(location)
                locationManager?.removeUpdates(this)
            }

            override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }

        try {
            locationManager?.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER,
                locationListener,
                null
            )
        } catch (ex: SecurityException) {
            // Handle location permission not granted
        }
    }

    fun getCityName(location: Location): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses =
            geocoder.getFromLocation(location.latitude, location.longitude, 1) ?: emptyList()
        return if (addresses.isNotEmpty()) {
            addresses[0].locality ?: ""
        } else {
            ""
        }
    }
}
