package sdr.tecqza.homedelivery_deliveryboy.ui.order

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val orderList = ArrayList<Order>()

    fun setList(order: ArrayList<Order>) {
        orderList.clear()
        orderList.addAll(order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ListOrderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

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
                    orderStatus(order.orderId!!)
                }
                call.setOnClickListener {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:" + order.mobile)
                    it.context.startActivity(intent)
                }
                cancel.setOnClickListener {
                    MaterialDialog(it.context).show {
                        input { dialog, text ->
                        }
                        positiveButton(R.string.send_otp)
                    }
                    cancelOrderStatus(order.orderId!!,"reason")
                }
                if (order.status == "Delivered")
                    pendingGroup.visibility = View.GONE
                 else
                    delivered.visibility = View.GONE
            }

        }

        private fun orderStatus(orderId: String) {
            val orderStatus = RiderService.create().orderStatus(orderId)
//            orderStatus.enqueue(object : Callback<>)
        }

        private fun cancelOrderStatus(orderId: String, reason: String) {
            val cancelOrderStatus = RiderService.create().cancelOrderStatus(orderId,reason)
            cancelOrderStatus.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            })
        }
    }
}