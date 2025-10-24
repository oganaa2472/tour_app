package com.example.survey.data.remote.network

import com.example.survey.data.remote.api.TourApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor
class NetworkModule {

    private val BASE_URL = "http://10.0.2.2:3000/api/v1/"
    private val BASE_IMAGE_URL = "http://10.0.2.2:3000/img/tours/"


    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val request = chain.request()
            // TODO: Add authentication header if needed
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun provideTourApiService(retrofit: Retrofit): TourApiService {
        return retrofit.create(TourApiService::class.java)
    }

    fun getBaseImageUrl(): String {
        return BASE_IMAGE_URL
    }
}