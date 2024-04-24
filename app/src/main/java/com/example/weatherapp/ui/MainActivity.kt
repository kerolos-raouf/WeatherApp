package com.example.weatherapp.ui


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.R
import com.example.weatherapp.data.models.WeatherItem
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority

import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    ///location service
    private val REQUEST_CODE_FOR_GPS = 1
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    ///view and viewModels
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()


    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(!it)
        {
            Toast.makeText(this,"You must approve location permission to get weather information.",Toast.LENGTH_LONG).show()
        }
        else
        {
            //Toast.makeText(this,"Granted",Toast.LENGTH_LONG).show()
            //getLocation()
            openGPSOnApprovingPermission()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        //Toast.makeText(this,text,Toast.LENGTH_LONG).show()

        initValues()
        setUp()
    }

    private fun initValues()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }
    private fun setUp() {
        setUpRecyclerView()
        checkPermission()
        observeOnError()
        refreshData()
    }

    private fun refreshData()
    {
        binding.refreshView.setOnRefreshListener {
            if (isThereInternetConnection())
            {
                checkPermission()
            }else
            {
                Toast.makeText(this,"There no internet connection and this is a fake data or an old data.",Toast.LENGTH_LONG).show()
            }
            binding.refreshView.isRefreshing = false
        }
    }

    private fun observeOnError() {
        viewModel.errorMessage.observe(this){
            if(it != null)
            {
                Toast.makeText(this,it,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpRecyclerView()
    {
        val mAdapter = MainAdapter(emptyList(),object : ItemInteractionListener{
            override fun onItemClicked(item : WeatherItem) {
                viewModel.mainItemValues.postValue(item)
            }
        })
        binding.mainRecyclerView.adapter = mAdapter
    }





    private fun checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            openGPSOnApprovingPermission()
        }else
        {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }


    private fun openGPSOnApprovingPermission()
    {
        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task.addOnSuccessListener {respone->

            val state = respone.locationSettingsStates
            //Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
            if(state!!.isLocationPresent)
            {
                //Toast.makeText(this,"GPS is opened.",Toast.LENGTH_LONG).show()
                getLocation()
            }

        }.addOnFailureListener {
            val statusCode = (it as ResolvableApiException).statusCode
            if(statusCode ==  LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
            {
                try {
                    Toast.makeText(this,"GPS is off you have to turn it on.",Toast.LENGTH_LONG).show()
                    it.startResolutionForResult(this,REQUEST_CODE_FOR_GPS)
                }catch (e : IntentSender.SendIntentException)
                {
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_FOR_GPS && resultCode == RESULT_CANCELED)
        {
            Toast.makeText(this,"You must open GPS to get weather information.",Toast.LENGTH_LONG).show()
        }
        if(requestCode == REQUEST_CODE_FOR_GPS && resultCode == RESULT_OK)
        {
            getLocation()
        }
    }


    private fun getLocation()
    {
        if(isGPSEnabled())
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

            {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{
                    val location = it.result
                    if(location == null)
                    {
                        val locationRequest = LocationRequest().apply {
                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            interval = 0
                            fastestInterval = 0
                            numUpdates = 2
                        }
                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,object : LocationCallback(){
                                override fun onLocationResult(p0: LocationResult) {
                                    val lastLocation = p0.lastLocation
                                    //Toast.makeText(applicationContext,"${location!!.latitude} ${location.longitude}",Toast.LENGTH_LONG).show()
                                    if(lastLocation != null)
                                    {
                                        //Toast.makeText(applicationContext,"${location.latitude} ${location.longitude}",Toast.LENGTH_LONG).show()
                                        setViewModelLocationValues(lastLocation.latitude,lastLocation.longitude)
                                    }
                                }
                            }, Looper.myLooper())
                    }else
                    {
                        //Toast.makeText(this,"${location.latitude} ${location.longitude}",Toast.LENGTH_LONG).show()
                        setViewModelLocationValues(location.latitude,location.longitude)
                    }
                }
            }

        }else
        {
            Toast.makeText(this,"You must open GPS to get weather information.",Toast.LENGTH_LONG).show()
        }

    }

    private fun setViewModelLocationValues(lat: Double,long: Double)
    {
        viewModel.latitude.postValue(lat)
        viewModel.longitude.postValue(long)
        setCountryNameAndCityName(lat,long)
        if(isThereInternetConnection())
        {
            viewModel.getDataFromWeatherApi()
        }else
        {
            Toast.makeText(this,"There no internet connection and this is a fake data or an old data.",Toast.LENGTH_LONG).show()
        }
    }

    private fun isThereInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected
        }
        return false
    }

    private fun setCountryNameAndCityName(lat : Double,long : Double)
    {
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val address = geocoder.getFromLocation(lat,long,1)
        viewModel.countryName.postValue(address?.get(0)?.countryName)
        viewModel.cityName.postValue(address?.get(0)?.locality)
        //Toast.makeText(applicationContext,"${address?.get(0)?.countryName} ${address?.get(0)?.locality}",Toast.LENGTH_LONG).show()
    }

    private fun isGPSEnabled() : Boolean
    {
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}