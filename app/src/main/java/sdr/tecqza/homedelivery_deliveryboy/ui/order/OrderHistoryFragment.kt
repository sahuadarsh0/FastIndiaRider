package sdr.tecqza.homedelivery_deliveryboy.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
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
    val args : OrderHistoryFragmentArgs by navArgs()

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
        if (args.status!= null){
            getOrder(args.status)
        }


        return root
    }

    private fun getOrder(status: String) {
        val order = RiderService.create().order(userSharedPreferences["riderId"], status)
        order.enqueue(object : Callback<List<Order?>?> {
            override fun onResponse(call: Call<List<Order?>?>, response: Response<List<Order?>?>) {

                val orders = response.body()
                Log.d("asa", "onResponse: ${orders?.size}")

            }

            override fun onFailure(call: Call<List<Order?>?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}