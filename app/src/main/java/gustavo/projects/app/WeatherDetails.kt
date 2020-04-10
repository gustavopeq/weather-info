package gustavo.projects.app

data class WeatherDetails(var cityAndCountry : String = "",
                          var weather : String = "",
                          var temperature : String = "",
                          var feelsLike : String = "",
                          var weatherIconJsonName : String = "")