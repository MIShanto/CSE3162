package bd.ac.ru.cse3162

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class MainActivity : AppCompatActivity() {

    // FusedLocationProviderClient - Main class for receiving location updates.
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var btn : Button

    var lat = 0.0
    var lon = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        btn = findViewById(R.id.bSearch)
        btn.setOnClickListener {
            getlocation()
        }
    }

    private fun getlocation() {
        try {
            var task = fusedLocationProviderClient.lastLocation

            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
            task.addOnSuccessListener {
                if (it != null){
                    lat = it.latitude
                    lon = it.longitude

                    var cityName = getCity(lat, lon)
                    var countryName = getCountry(lat, lon)

                    val latText : TextView= findViewById(R.id.Lat_text)
                    latText.text = "Latitude   "+lat

                    val longTxt : TextView= findViewById(R.id.Long_text)
                    longTxt.text = "Longitude   "+lon

                    val locationTxt : TextView= findViewById(R.id.City_text)
                    locationTxt.text = cityName+", "+countryName

                    val getWeatherButton : Button = findViewById(R.id.View_weather_btn)

                    getWeatherButton.setOnClickListener{
                        
                    }

                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }

    }

    private fun getCity(lat: Double, lng: Double): String {
        // Function to get location city name using geocoder
        var geocoder = Geocoder(this, Locale.ENGLISH) // initializing geocoder for the context
        var list = geocoder.getFromLocation(lat, lng, 1) // getting location using lat long
        return list[0].locality  // as it returns a list object we fetch only the local name
    }
    private fun getCountry(lat: Double, lng: Double): String {
        // Function to get location city name using geocoder
        var geocoder = Geocoder(this, Locale.ENGLISH) // initializing geocoder for the context
        var list = geocoder.getFromLocation(lat, lng, 1) // getting location using lat long
        return list[0].countryName  // as it returns a list object we fetch only the local name
    }

}