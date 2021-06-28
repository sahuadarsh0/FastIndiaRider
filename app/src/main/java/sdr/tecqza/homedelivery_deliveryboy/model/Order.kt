package sdr.tecqza.homedelivery_deliveryboy.model

import com.google.gson.annotations.SerializedName

data class Order(

    @field:SerializedName("order_no")
    val orderNo: String? = null,

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("mobile")
    val mobile: String? = null,

    @field:SerializedName("customer_id")
    val customerId: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("rider_id")
    val riderId: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
