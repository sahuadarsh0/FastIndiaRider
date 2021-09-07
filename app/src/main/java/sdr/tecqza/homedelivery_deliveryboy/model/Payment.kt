package sdr.tecqza.homedelivery_deliveryboy.model

import com.google.gson.annotations.SerializedName

data class Payment(

	@field:SerializedName("special_allowance")
	val specialAllowance: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("net_salary")
	val netSalary: String? = null,

	@field:SerializedName("other")
	val other: String? = null,

	@field:SerializedName("bonus")
	val bonus: String? = null,

	@field:SerializedName("basic_pay")
	val basicPay: String? = null,

	@field:SerializedName("incentive")
	val incentive: String? = null,

	@field:SerializedName("month")
	val month: String? = null,

	@field:SerializedName("rec_no")
	val recNo: String? = null,

	@field:SerializedName("hra")
	val hra: String? = null,

	@field:SerializedName("pf")
	val pf: String? = null,

	@field:SerializedName("other_allowance")
	val otherAllowance: String? = null,

	@field:SerializedName("lta")
	val lta: String? = null,

	@field:SerializedName("esi")
	val esi: String? = null,

	@field:SerializedName("pay_id")
	val payId: String? = null,

	@field:SerializedName("rider_id")
	val riderId: String? = null,

	@field:SerializedName("da")
	val da: String? = null
)
