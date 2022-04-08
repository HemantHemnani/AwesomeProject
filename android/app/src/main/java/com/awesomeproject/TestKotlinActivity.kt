package com.awesomeproject

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.awesomeproject.databinding.ActivityTestKotlinBinding
import com.facebook.react.ReactActivity
import com.facebook.react.bridge.Arguments
import com.facebook.react.common.MapBuilder
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider


class TestKotlinActivity : ReactActivity() {

    private val navigationLocationProvider = NavigationLocationProvider()
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource

    private lateinit var navigationCamera: NavigationCamera
    private val locationObserver = object : LocationObserver {

        override fun onNewRawLocation(rawLocation: Location) {
        }

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(
                enhancedLocation,
                locationMatcherResult.keyPoints,
            )
            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()
            updateCamera(enhancedLocation)
        }
    }


    private lateinit var mapboxMap: MapboxMap
    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var binding: ActivityTestKotlinBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapboxMap = binding.mapView.getMapboxMap()
        // Instantiate the location component which is the key component to fetch location updates.
        binding.mapView.location.apply {
            setLocationProvider(navigationLocationProvider)

            locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(
                    this@TestKotlinActivity,
                    R.drawable.navigation
                )
            )
            enabled = true
        }
        init()
    }
    private fun init() {
        initStyle()
        initNavigation()
    }

    @SuppressLint("MissingPermission")
    private fun initNavigation() {
        viewportDataSource = MapboxNavigationViewportDataSource(mapboxMap)
        navigationCamera = NavigationCamera(
            mapboxMap,
            binding.mapView.camera,
            viewportDataSource
        )

        mapboxNavigation = MapboxNavigation(
            NavigationOptions.Builder(this)
                .accessToken(getString(R.string.mapbox_access_token))
                .build()
        ).apply {
            startTripSession()
            registerLocationObserver(locationObserver)
        }
    }

    private fun initStyle() {
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS)
    }

    private fun updateCamera(location: Location) {
        val mapAnimationOptions = MapAnimationOptions.Builder().duration(1500L)
            .build()
//        Toast.makeText(this , "${location.latitude}  ${location.longitude}" , Toast.LENGTH_SHORT).show()
        val event = Arguments.createMap()
        event.putDouble("longitude", location.longitude)
        event.putDouble("latitude", location.latitude)
        val reactInstanceManager = reactNativeHost.reactInstanceManager
        val reactContext = reactInstanceManager.currentReactContext


        //        val context: ReactApplicationContext = getReactApplicationContext()
//        reactContext?.getJSModule(RCTEventEmitter::class.java)?.
//        receiveEvent(1, "onLocationChange", event)
        val map = MapBuilder.of("onLocationChange", MapBuilder.of("registrationName", "onLocationChange"))

        getReactInstanceManager().currentReactContext?.getJSModule(RCTDeviceEventEmitter::class.java)?.
                    emit("onLocationChange", event)

        binding.mapView.camera.easeTo(
            CameraOptions.Builder()
                .center(Point.fromLngLat(location.longitude, location.latitude))
                .zoom(18.0)
                .build(),
            mapAnimationOptions
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mapboxNavigation.stopTripSession()
        mapboxNavigation.unregisterLocationObserver(locationObserver)
    }

}