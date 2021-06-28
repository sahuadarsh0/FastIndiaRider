package sdr.tecqza.homedelivery_deliveryboy.ui.holiday

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sdr.tecqza.homedelivery_deliveryboy.R

class HolidayHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HolidayHistoryFragment()
    }

    private lateinit var viewModel: HolidayHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_holiday_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HolidayHistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}