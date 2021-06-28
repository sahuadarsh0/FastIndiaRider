package sdr.tecqza.homedelivery_deliveryboy.api

import android.util.Log
import sdr.tecqza.homedelivery_deliveryboy.model.Check
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Service {

    @FormUrlEncoded
    @POST("fastindia_permission")
    fun check(
        @Field("key") key: String?
    ): Call<Check>

    companion object {

        private var BASE_URL = "https://shashwatpublication.com/others/"

        fun create(): Service {
            val builder = OkHttpClient.Builder()
            val httpLoggingInterceptor = HttpLoggingInterceptor { s: String? -> Log.d("ASA", s!!) }

            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(httpLoggingInterceptor)

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(Service::class.java)
        }

    }
}

