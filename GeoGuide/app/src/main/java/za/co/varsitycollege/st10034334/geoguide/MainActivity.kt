package za.co.varsitycollege.st10034334.geoguide

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //GeeksForGeeks (2021) demonstrates retrieving current location.
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    //FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //LocationRequest - Requirements for the location updates, i.e.,
   //how often you should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    //LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback

    //This will store current location info.
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Gets the SupportMapFragment and set the callback for when the map is ready.
        //GeeksForGeeks (2023) demonstrates working with the Google Maps API.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupLocationUpdates()

    }

    //This method is called when the map is ready to be used.
    //GeeksForGeeks (2023) demonstrates working with the Google Maps API.
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Adds a marker in Sydney, Australia, and moves the camera.
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    //This function verifies that both coarse and fine location permissions are granted, otherwise, requests these
    //permissions from the user.
    //GeeksForGeeks (2021) demonstrates retrieving current location.
    private fun isLocationPermissionGranted(): Boolean {

        //Checks if the app has been granted coarse location permission and fine location permission.
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //If the app hasn't been granted these permissions, requests that they be granted.
            ActivityCompat.requestPermissions(
                this,
                //Array of permissions to request (coarse and fine location).
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),

                //Request code to track this specific permission request.
               LOCATION_PERMISSION_REQUEST_CODE
            )
            //Returns false as the permissions are not yet granted.
            false
        } else {

            //If both permissions are already granted, returns true.
            true
        }
    }

    //This function sets up everything to receive the user's location updates, but only if
    //the necessary permissions have been granted.
    //GeeksForGeeks (2021) demonstrates retrieving current location.
    @SuppressLint("MissingPermission")
    private fun setupLocationUpdates() {

        if (isLocationPermissionGranted()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            //Initializes locationRequest.
            locationRequest = LocationRequest().apply {
                //Sets the desired interval for active location updates.
                //This interval is inexact.
                interval = TimeUnit.SECONDS.toMillis(60)

                //Sets the fastest rate for active location updates.
                //This interval is exact, and the application will never receive updates more frequently than this value.
                fastestInterval = TimeUnit.SECONDS.toMillis(30)

                //Sets the maximum time when batched location updates are delivered.
                //Updates may be delivered sooner than this interval.
                maxWaitTime = TimeUnit.MINUTES.toMillis(2)

                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {

                    super.onLocationResult(locationResult)
                    locationResult.lastLocation?.let { location ->
                        currentLocation = location
                        var latitude = currentLocation?.latitude
                        var longitude = currentLocation?.longitude
                        Log.d(
                            "LOCATION TAG",
                            "Current location: Latitude: $latitude, Longitude: $longitude"
                        )

                        if (latitude != null && longitude != null)
                        {
                            updateMapCurrentLocation(latitude, longitude)
                        }

                    } ?: {
                        Log.d("LOCATION TAG", "Location information isn't available.")
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    private fun updateMapCurrentLocation(latitude: Double, longitude: Double) {

        //Adds a marker in current location and moves the camera.
        val currentLocation = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(currentLocation).title("Current Marker"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
    }


}
//REFERENCE LIST:
//GeeksForGeeks. 2021. How to Get Current Location in Android, 19 September 2021 (Version 2.0)
//[Source code] https://www.geeksforgeeks.org/how-to-get-current-location-in-android/
//(Accessed 4 October 2024).
//GeeksForGeeks. 2023. Google Maps in Android, 23 January 2023 (Version 2.0)
//[Source code] https://www.geeksforgeeks.org/google-maps-in-android/
//(Accessed 4 October 2024).