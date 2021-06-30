package sdr.tecqza.homedelivery_deliveryboy.ui.holiday

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.compose.ui.res.colorResource
import androidx.recyclerview.widget.RecyclerView
import sdr.tecqza.homedelivery_deliveryboy.databinding.ListHolidaysBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Holiday


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
                if (holiday.status == "Active")
                    card.setCardBackgroundColor(Color.parseColor("#FEBE14"))
                else
                    card.setCardBackgroundColor(Color.parseColor("#CECECE"))
            }

        }

    }
}

