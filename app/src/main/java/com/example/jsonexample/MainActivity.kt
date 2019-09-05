package com.example.jsonexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var weatherData: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherData = textView
        button.setOnClickListener { getCurrentData() }
    }

    private fun getCurrentData() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, AppId)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!
                    with(weatherResponse) {
                        val stringBuilder = "Country: " +
                                sys?.country +
                                "\n" +
                                "Temperature: " +
                                main?.temp +
                                "\n" +
                                "Temperature(Min): " +
                                main?.temp_min +
                                "\n" +
                                "Temperature(Max): " +
                                main?.temp_max +
                                "\n" +
                                "Humidity: " +
                                main?.humidity +
                                "\n" +
                                "Pressure: " +
                                main?.pressure
                        weatherData.text = stringBuilder
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                weatherData.text = t.message
            }
        })
    }

    companion object {

        var BaseUrl = "http://api.openweathermap.org/"
        var AppId = "2e65127e909e178d0af311a81f39948c"
        var lat = "35"
        var lon = "139"
    }
}
