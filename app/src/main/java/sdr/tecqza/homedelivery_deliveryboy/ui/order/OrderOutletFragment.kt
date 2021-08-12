package sdr.tecqza.homedelivery_deliveryboy.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import retrofit2.Call
import retrofit2.Callback
import sdr.tecqza.homedelivery_deliveryboy.R
import sdr.tecqza.homedelivery_deliveryboy.api.RiderService
import sdr.tecqza.homedelivery_deliveryboy.databinding.FragmentOrderOutletBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Response
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs

class OrderOutletFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private var _binding: FragmentOrderOutletBinding? = null
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

        _binding = FragmentOrderOutletBinding.inflate(inflater, container, false)
        val root: View = binding.root

        processDialog = ProcessDialog(requireContext())
        userSharedPreferences = SharedPrefs(requireContext(), "USER")
        binding.apply {
            submit.setOnClickListener {
                if (mobile.text.isNotEmpty() || orderNo.text.isNotEmpty() || customerName.text.isNotEmpty() || !mobile.text
                    .matches(Regex("^[6-9][0-9]{9}$"))
                ) {
                    processDialog.show()
                    send(orderNo.text.toString(), customerName.text.toString(), mobile.text.toString())
                } else {
                    Toast.makeText(requireContext(), "Fill All details properly", Toast.LENGTH_SHORT).show()
                }
            }
            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun send(orderNo: String, customerName: String, mobile: String) {
        val entry = RiderService.create().entry(
            orderNo = orderNo,
            name = customerName,
            mobile = mobile,
            riderId = userSharedPreferences["riderId"],
            stateId = userSharedPreferences["stateId"],
            cityId = userSharedPreferences["cityId"]
        )
        entry.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    when (response.body()?.error) {
                        "0" -> {
                            Toast.makeText(requireContext(), "Order Added Successfully", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.navigation_home)
                        }
                        "1" -> Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(requireContext(), "Error Not Added", Toast.LENGTH_SHORT).show()
                    }
                }

                processDialog.dismiss()
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                MaterialDialog(requireContext()).show {
                    title(text = "API ERROR")
                    message(text = "Order Not Cancelled")
                    cornerRadius(16f)
                    positiveButton(text = "Yes") { dialog ->
                        dialog.dismiss()
                    }
                }
                processDialog.dismiss()
            }
        })
    }
}
