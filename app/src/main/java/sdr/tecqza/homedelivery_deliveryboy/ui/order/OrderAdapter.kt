package sdr.tecqza.homedelivery_deliveryboy.ui.order

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sdr.tecqza.homedelivery_deliveryboy.R
import sdr.tecqza.homedelivery_deliveryboy.api.RiderService
import sdr.tecqza.homedelivery_deliveryboy.databinding.ListOrderBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Order
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs


class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val orderList = ArrayList<Order>()
    private lateinit var processDialog: ProcessDialog
    private lateinit var userSharedPreferences: SharedPrefs

    fun setList(order: ArrayList<Order>?) {
        orderList.clear()
        orderList.addAll(order!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ListOrderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        processDialog = ProcessDialog(parent.context)
        userSharedPreferences = SharedPrefs(parent.context, "USER")
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }


    inner class OrderViewHolder(private val binding: ListOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                orderNo.text = order.orderNo
                customerName.text = order.name
                mobile.text = order.mobile
                date.text = order.date
                pending.setOnClickListener {
                    val randomPin = (Math.random() * 9000).toInt() + 1000
                    MaterialDialog(it.context).show {
                        title(text = "Otp")
                        message(text = "Send OTP to ${order.mobile} ?")
                        cornerRadius(16f)
                        positiveButton(text = "Yes") { dialog ->
                            sendOTP(
                                order.mobile!!,
                                randomPin.toString(),
                                order.orderId!!
                            )
                            dialog.dismiss()
                        }
                    }

                }

                call.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:" + order.mobile)
                    it.context.startActivity(intent)
                }

                cancel.setOnClickListener {

                    MaterialDialog(itemView.context).show {
                        title(text = "Reason")
                        message(text = "Enter reason for Cancellation")
                        cornerRadius(16f)

                        input { dialog, text ->

                            if (text.toString().isNullOrBlank())
                                Toast.makeText(itemView.context, "Reason cannot be empty", Toast.LENGTH_SHORT).show()
                            else
                                cancelOrderStatus(order.orderId!!, text.toString())

                        }
                        positiveButton(R.string.submit)
                    }
                }

                when(order.status){
                    "Pending"->{
                        delivered.visibility = View.GONE
                        canceled.visibility = View.GONE
                    }
                    "Delivered"->{
                        canceled.visibility = View.GONE
                        pendingGroup.visibility = View.GONE
                    }
                    "Cancelled"->{
                        delivered.visibility = View.GONE
                        pendingGroup.visibility = View.GONE
                    }
                }
            }

        }

        private fun sendOTP(mobile: String, otp: String, orderId: String) {
            val sendOTP = RiderService.create().sendOtp(mobile, otp)
            sendOTP.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        MaterialDialog(itemView.context).show {
                            title(text = "Otp")
                            message(text = "Enter OTP received by customer")
                            cornerRadius(16f)

                            input { dialog, text ->

                                Log.d("asa", "text  $text = > OTP $otp")
                                if (text.toString() == otp) {
                                    Log.d("asa", "onResponse: OTP matched $text = > $otp")
                                    orderStatus(orderId)
                                } else
                                    Toast.makeText(itemView.context, "OTP not matched", Toast.LENGTH_SHORT).show()

                            }
                            positiveButton(R.string.submit)
                        }
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("asa", "onFailure: ${t.message}")
                }
            })

        }

        private fun orderStatus(orderId: String) {
            processDialog.show()
            val orderStatus = RiderService.create().orderStatus(orderId)
            orderStatus.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    if (response.isSuccessful) {
                        Toast.makeText(itemView.context, "Order Delivered", Toast.LENGTH_SHORT).show()
                        itemView.findNavController().navigate(R.id.navigation_home)
                    }
                    processDialog.dismiss()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    processDialog.dismiss()
                    Toast.makeText(itemView.context, "Error Not Delivered", Toast.LENGTH_SHORT).show()

                }
            })
        }

        private fun cancelOrderStatus(orderId: String, reason: String) {
            processDialog.show()
            val cancelOrderStatus = RiderService.create().cancelOrderStatus(orderId, reason)
            cancelOrderStatus.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    if (response.isSuccessful)
                        Log.d("asa", "order cancelled")
                    Toast.makeText(itemView.context, "Order cancelled", Toast.LENGTH_SHORT).show()
                    itemView.findNavController().navigate(R.id.navigation_home)
                    processDialog.dismiss()
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(itemView.context, "Error Order not cancelled", Toast.LENGTH_SHORT).show()

                    processDialog.dismiss()
                }


            })
        }
    }
}