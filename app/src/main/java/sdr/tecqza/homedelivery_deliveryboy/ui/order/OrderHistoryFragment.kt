package sdr.tecqza.homedelivery_deliveryboy.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sdr.tecqza.homedelivery_deliveryboy.api.RiderService
import sdr.tecqza.homedelivery_deliveryboy.databinding.FragmentOrderHistoryBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Order
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs

class OrderHistoryFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var userSharedPreferences: SharedPrefs
    private lateinit var processDialog: ProcessDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        orderViewModel =
            ViewModelProvider(this).get(OrderViewModel::class.java)

        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        processDialog = ProcessDialog(requireContext())
        userSharedPreferences = SharedPrefs(requireContext(), "USER")

        return root
    }

    private fun getOrder(status: String) {
        val order = RiderService.create().order(userSharedPreferences["riderId"], status)
        order.enqueue(object : Callback<Order?> {
            override fun onResponse(call: Call<Order?>, response: Response<Order?>) {

//                val orders =

            }

            override fun onFailure(call: Call<Order?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}