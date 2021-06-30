package sdr.tecqza.homedelivery_deliveryboy.ui.holiday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sdr.tecqza.homedelivery_deliveryboy.api.RiderService
import sdr.tecqza.homedelivery_deliveryboy.databinding.FragmentHolidayHistoryBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Holiday
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs

class HolidayHistoryFragment : Fragment() {
    private lateinit var holidayHistoryViewModel: HolidayHistoryViewModel
    private var _binding: FragmentHolidayHistoryBinding? = null
    private lateinit var userSharedPreferences: SharedPrefs
    private lateinit var processDialog: ProcessDialog
    private val adapter = HolidayAdapter()

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

        processDialog = ProcessDialog(requireContext())
        userSharedPreferences = SharedPrefs(requireContext(), "USER")

        binding.holidayList.layoutManager = GridLayoutManager(context, 2)
        binding.holidayList.adapter = adapter
        processDialog.show()
        getRiderHoliday(userSharedPreferences["riderId"])
        return root
    }

    private fun getRiderHoliday(riderId: String?) {
        val getRiderHoliday = RiderService.create().holiday(riderId)
        getRiderHoliday.enqueue(object : Callback<ArrayList<Holiday>?> {
            override fun onResponse(call: Call<ArrayList<Holiday>?>, response: Response<ArrayList<Holiday>?>) {


                val holidays = response.body()
                adapter.setList(holidays)
                adapter.notifyDataSetChanged()
                processDialog.dismiss()

            }

            override fun onFailure(call: Call<ArrayList<Holiday>?>, t: Throwable) {
                processDialog.dismiss()
                TODO("Not yet implemented")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}