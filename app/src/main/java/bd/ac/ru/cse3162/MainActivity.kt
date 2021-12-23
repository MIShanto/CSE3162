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
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

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
                        getWeather(cityName)
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

    private fun getWeather(cityName : String)
    {
        var url = "https://api.weatherbit.io/v2.0/current?city=${cityName}&key=3e34fa6371b84c09aa576df683dc70dc&include=minutely"

        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response)
            {
                var jsonString = response.body?.string()

                ParseJson(jsonString)

            }
        })
    }

    private fun ParseJson(jsonString: String?) {

        // get JSONObject from JSON file
        val obj = JSONObject(jsonString)

        // fetch JSONObject named weather
        val jsonArray: JSONArray = obj.getJSONArray("data")
        val weatherJsonObject : JSONObject = jsonArray.getJSONObject(0)
        val weatherDescription : JSONObject = weatherJsonObject.getJSONObject("weather")
        val weathertxt : String = weatherDescription.getString("description")

        Log.d("TAG", "ParseJson: "+weatherDescription)

        var weatherText : TextView = findViewById(R.id.textView10)


       // val mainJSONObject : JSONObject = obj.getJSONObject("main")
        val temp : String = (weatherJsonObject.getString("temp"))
        val sunrise : String = (weatherJsonObject.getString("sunrise"))
        val sunset : String = (weatherJsonObject.getString("sunset"))
        val humidity : String = (weatherJsonObject.getString("rh")) + " %"
        val pressure : String = (weatherJsonObject.getString("pres")) + " mb"
        val visibility : String = (weatherJsonObject.getString("vis")) + " KM"
        val dateTime : String = (weatherJsonObject.getString("datetime"))

      //  val windJSONObject : JSONObject = obj.getJSONObject(    "wind")
      //  val windSpeed : String = windJSONObject.getString("speed") // meter/sec


        runOnUiThread{
            weatherText.text = weathertxt
            findViewById<TextView>(R.id.Celcius_txt).text = temp+"\u2103"
            findViewById<TextView>(R.id.Farenheit_txt).text = temp+"\u2109"
            findViewById<TextView>(R.id.tv_sunrise_time).text = sunrise
            findViewById<TextView>(R.id.tv_sunset_time).text = sunset
            findViewById<TextView>(R.id.humidity_txt).text = "আর্দ্রতা  " + humidity
            findViewById<TextView>(R.id.pressure_txt).text = "বায়ু চাপ  "+pressure
            findViewById<TextView>(R.id.visibility_txt).text = "দৃষ্টিসীমা  "+visibility
            findViewById<TextView>(R.id.Date_Time_txt).text = dateTime

        }



    }

}