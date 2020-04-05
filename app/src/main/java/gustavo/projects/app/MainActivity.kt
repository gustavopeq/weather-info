package gustavo.projects.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

    private lateinit var cityEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var cityDescriptionTextView: TextView
    private lateinit var weatherDescriptionTextView: TextView
    private lateinit var tempDescriptionTextView: TextView
    private lateinit var feelsLikeDescriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.cityEditText = findViewById(R.id.cityEditText)
        this.countryEditText = findViewById(R.id.countryEditText)
        this.cityDescriptionTextView = findViewById(R.id.cityDescriptionTextView)
        this.weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView)
        this.tempDescriptionTextView = findViewById(R.id.TempDescriptionTextView)
        this.feelsLikeDescriptionTextView = findViewById(R.id.FeelsLikeDescriptionTextView)

        val infoButton : Button = findViewById(R.id.infoButton)

        infoButton.setOnClickListener(View.OnClickListener { getWeather() })

    }

    private fun getWeather() {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OpenWeatherInterface::class.java)
        val call = service.getCurrentWeatherByCity(cityEditText.text.toString(), APP_KEY)

        call.enqueue(object : Callback<OpenWeatherResponse>{
            override fun onFailure(call: Call<OpenWeatherResponse>, t: Throwable) {
                Toast.makeText(baseContext, t?.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OpenWeatherResponse>,
                response: Response<OpenWeatherResponse>
            ) {
                if(response?.code() == 200)
                {
                    val responseWeather = response.body()!!

                    cityDescriptionTextView.text = "${responseWeather.name} (${responseWeather.sys.country})"
                    weatherDescriptionTextView.text = "${responseWeather.weather[0].main} (${responseWeather.weather[0].description})"
                    tempDescriptionTextView.text = "${responseWeather.main.temp} C"
                    feelsLikeDescriptionTextView.text = "${responseWeather.main.feelsLike} C"
                }
            }

        })
    }

}
