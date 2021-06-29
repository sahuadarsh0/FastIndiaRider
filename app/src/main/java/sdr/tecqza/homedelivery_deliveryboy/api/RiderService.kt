package sdr.tecqza.homedelivery_deliveryboy.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import sdr.tecqza.homedelivery_deliveryboy.model.*

interface RiderService {

    @FormUrlEncoded
    @POST("rider/login")
    fun login(@Field("mobile") mobile: String?): Call<Response?>?


    @FormUrlEncoded
    @POST("customer/cancelOrderStatus")
    fun cancelOrderStatus(
        @Field("order_id") order_id: String?,
        @Field("reason") reason: String?,
    ): Call<ResponseBody?>

    @GET("customer/order/{rider_id}/{status}")
    fun order(
        @Path("rider_id") riderId: String?,
        @Path("status") status: String?,
    ): Call<ArrayList<Order?>?>

    @GET("customer/index/{rider_id}")
    fun index(
        @Path("rider_id") riderId: String?
    ): Call<ArrayList<Order?>?>

    @FormUrlEncoded
    @POST("customer/orderStatus")
    fun orderStatus(
        @Field("order_id") order_id: String?
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("rider/updateStatus")
    fun updateStatus(
        @Field("mobile") mobile: String?
    ): Call<Response>


    @GET("rider/holiday/{rider_id}")
    fun holiday(
        @Path("rider_id") riderId: String?
    ): Call<List<Holiday?>?>

    @GET("customer/orderCount/{rider_id}")
    fun orderCount(
        @Path("rider_id") riderId: String?
    ): Call<Count?>


    @GET("rider/appVer")
    fun checkUpdate(): Call<CheckUpdate>


    companion object {

        private var BASE_URL = "http://fastindia.app/api/"
        var RIDER_URL = "http://fastindia.app/uploads/rider/"

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
