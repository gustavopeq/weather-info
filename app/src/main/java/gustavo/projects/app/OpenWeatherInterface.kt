package gustavo.projects.app

import gustavo.projects.app.model.OpenWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherInterface {
    @GET("data/2.5/weather?")
    fun getCurrentWeatherByCityAndCountry(@Query("q") cityAndCountry : String,
                                          @Query("APPID") app_id: String,
                                          @Query("units") units: String = "metric") : Call<OpenWeatherResponse>
}