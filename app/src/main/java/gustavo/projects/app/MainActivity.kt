package gustavo.projects.app

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import gustavo.projects.app.databinding.ActivityMainBinding
import gustavo.projects.app.model.OpenWeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object{
        const val BASE_URL = "http://api.openweathermap.org/"
        const val APP_KEY = "47ac5893a42b55d83cdf65df008e9d57"
    }

    private lateinit var binding: ActivityMainBinding
    private val weatherDetails: WeatherDetails = WeatherDetails()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.weatherDetails = weatherDetails

        binding.infoButton.setOnClickListener(View.OnClickListener { getWeather(it) })
    }

    private fun getWeather(view: View) {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OpenWeatherInterface::class.java)
        val concatenatedCityCountry =  binding.cityEditText.text.toString()+","+ binding.countryEditText.text.toString()
        val call = service.getCurrentWeatherByCityAndCountry(concatenatedCityCountry, APP_KEY)

        call.enqueue(object : Callback<OpenWeatherResponse>{
            override fun onFailure(call: Call<OpenWeatherResponse>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OpenWeatherResponse>,
                response: Response<OpenWeatherResponse>
            ) {
                if(response.code() == 200)
                {
                    val responseWeather = response.body()!!

                    binding.apply {
                        weatherDetails?.cityAndCountry = "${responseWeather.name} (${responseWeather.sys.country})"
                        weatherDetails?.weather = "${responseWeather.weather[0].main} (${responseWeather.weather[0].description})"
                        weatherDetails?.temperature = "${responseWeather.main.temp} C"
                        weatherDetails?.feelsLike = "${responseWeather.main.feelsLike} C"
                        when (responseWeather.weather[0].id) {
                            in 200..232 -> weatherDetails?.weatherIconJsonName = "thunderstorm.json"
                            in 300..531 -> weatherDetails?.weatherIconJsonName = "rain.json"
                            in 600..622 -> weatherDetails?.weatherIconJsonName = "snow.json"
                            in 701..781 -> weatherDetails?.weatherIconJsonName = "mist.json"
                            in 801..804 -> weatherDetails?.weatherIconJsonName = "cloudy.json"
                            else -> weatherDetails?.weatherIconJsonName = "clearSky.json"
                        }
                        binding.weatherIcon.setAnimation(weatherDetails?.weatherIconJsonName)

                        binding.descriptionPanelLayout.visibility = View.VISIBLE

                        invalidateAll()
                    }
                }
            }

        })

        //Hide the keyboard
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
