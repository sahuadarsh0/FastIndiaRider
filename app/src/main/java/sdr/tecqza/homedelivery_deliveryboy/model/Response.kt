package sdr.tecqza.homedelivery_deliveryboy.model

import com.google.gson.annotations.SerializedName

data class Response(
    val data: Data? = null,
    val otp: Int? = null,
    val error: String? = null,
    val message: String? = null
)

data class Data(

    @field:SerializedName("email_id")
    val emailId: String? = null,
    val image: String? = null,
    val address: String? = null,
    val mobile: String? = null,
    val name: String? = null,
    val status: String? = null,
    val photoUrl: String? = null,

    @field:SerializedName("state_id")
    val stateId: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("city_id")
    val cityId: String? = null,

    @field:SerializedName("rider_id")
    val riderId: String? = null
)
