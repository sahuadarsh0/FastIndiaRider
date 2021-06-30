package sdr.tecqza.homedelivery_deliveryboy.ui.holiday

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
import sdr.tecqza.homedelivery_deliveryboy.databinding.ListHolidaysBinding
import sdr.tecqza.homedelivery_deliveryboy.databinding.ListOrderBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Holiday
import sdr.tecqza.homedelivery_deliveryboy.model.Order
import sdr.tecqza.homedelivery_deliveryboy.ui.order.OrderAdapter


class HolidayAdapter : RecyclerView.Adapter<HolidayAdapter.HolidayViewHolder>() {

    private val holidayList = ArrayList<Holiday>()

    fun setList(holiday: ArrayList<Holiday>?) {
        holidayList.clear()
        holidayList.addAll(holiday!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayAdapter.HolidayViewHolder {
        val binding = ListHolidaysBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return HolidayViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return holidayList.size
    }

    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {
        holder.bind(holidayList[position])
    }


    inner class HolidayViewHolder(private val binding: ListHolidaysBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(holiday: Holiday) {
            binding.apply {
                date.text = holiday.date
            }

        }

    }
}