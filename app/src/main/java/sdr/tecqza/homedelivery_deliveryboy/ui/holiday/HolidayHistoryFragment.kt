package sdr.tecqza.homedelivery_deliveryboy.ui.holiday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import sdr.tecqza.homedelivery_deliveryboy.databinding.FragmentHolidayHistoryBinding
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs

class HolidayHistoryFragment : Fragment() {
    private lateinit var holidayHistoryViewModel: HolidayHistoryViewModel
    private var _binding: FragmentHolidayHistoryBinding? = null
    private lateinit var userSharedPreferences: SharedPrefs
    private lateinit var processDialog: ProcessDialog

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        holidayHistoryViewModel =
            ViewModelProvider(this).get(HolidayHistoryViewModel::class.java)

        _binding = FragmentHolidayHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}