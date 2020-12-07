package com.example.forecast.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.example.forecast.data.api.response.CurrentWeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//api key keep private
const val API_KEY = "5b2f769e9296b37bd27ac5ef258420bf"

//http://api.weatherstack.com/current?access_key=5b2f769e9296b37bd27ac5ef258420bf&query=Rawalpindi&lang=en

/*
 * Weather stack API interface (From weather stack documentation)
 */
interface WeatherStackApiService {

    @GET("current")
    fun getCurrentWeatherAsync(@Query("query") location: String, @Query("lang") languageCode: String = "en"): Deferred<CurrentWeatherResponse>

    //object takes in the interceptor
    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): WeatherStackApiService {
            val requestInterceptor = Interceptor {
                //get the request url
                val url = it.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()
                //build the request with the url we just made above
                val request = it.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor it.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            //use retrofit to build call
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherStackApiService::class.java)
        }
    }
}
