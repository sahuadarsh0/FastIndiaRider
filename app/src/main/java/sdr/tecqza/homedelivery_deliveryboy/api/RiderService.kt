package sdr.tecqza.homedelivery_deliveryboy.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import sdr.tecqza.homedelivery_deliveryboy.model.CheckUpdate

interface RiderService {

    @FormUrlEncoded
    @POST("customer/login")
    fun login(@Field("mobile") mobile: String?): Call<Response?>?


    @FormUrlEncoded
    @POST("customer/cancelOrderStatus")
    fun cancelOrderStatus(
        @Field("order_id") order_id: String?,
        @Field("reason") reason: String?,
    ): Call<ResponseBody?>

    @FormUrlEncoded
    @POST("customer/register")
    fun customerRegister(
        @Field("name") name: String?,
        @Field("mobile") mobile: String?,
        @Field("email_id") emailId: String?,
        @Field("address") address: String?,
        @Field("state_id") stateId: String?,
        @Field("city_id") cityId: String?
    ): Call<Response>

    @GET("customer/appVer")
    fun checkUpdate(): Call<CheckUpdate>


    companion object {

        var BASE_URL = "http://fastindia.app/api/"
        var ASSETS_URL = "http://fastindia.app/uploads/customer/"

        fun create(): RiderService {

            val builder = OkHttpClient.Builder()
            val httpLoggingInterceptor = HttpLoggingInterceptor { s: String? -> Log.d("asa", s!!) }

            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(httpLoggingInterceptor)

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())

                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(RiderService::class.java)
        }
    }
}
