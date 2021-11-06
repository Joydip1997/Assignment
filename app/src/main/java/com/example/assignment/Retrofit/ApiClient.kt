package com.example.assignment.Retrofit




import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {
    const val IMAGE_URL =
        "https://engageandroidpics23606-android.s3.ap-south-1.amazonaws.com/public/"



    private const val BASE_URL = "https://testscoretracker.herokuapp.com/"


    private lateinit var api: Api

    @JvmStatic
    fun getmRetrofitInstance(): Api {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        var okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .callTimeout(1, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(Api::class.java)
        return api
    }


}