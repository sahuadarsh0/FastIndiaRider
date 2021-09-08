package sdr.tecqza.homedelivery_deliveryboy.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sdr.tecqza.homedelivery_deliveryboy.api.RiderService
import sdr.tecqza.homedelivery_deliveryboy.databinding.FragmentPaymentHistoryBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Payment
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs

class PaymentHistoryFragment : Fragment() {

    private var _binding: FragmentPaymentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var userSharedPreferences: SharedPrefs
    private lateinit var processDialog: ProcessDialog
    private var payments: ArrayList<Payment>? = null
    private val adapter = PaymentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        processDialog = ProcessDialog(requireContext())
        userSharedPreferences = SharedPrefs(requireContext(), "USER")

        binding.paymentsList.layoutManager = GridLayoutManager(context, 2)
        binding.paymentsList.adapter = adapter
        processDialog.show()

        getAllPayment()

        binding.advanced.setOnClickListener {
            startC()
        }
        return root
    }

    private fun startC() {
    }

    private fun getAllPayment() {
        val order = RiderService.create().payment("21")
//        val order = RiderService.create().payment(userSharedPreferences["riderId"])
        order.enqueue(object : Callback<ArrayList<Payment>?> {
            override fun onResponse(call: Call<ArrayList<Payment>?>, response: Response<ArrayList<Payment>?>) {
                payments = response.body()
                adapter.setList(payments)
                adapter.notifyDataSetChanged()
                processDialog.dismiss()
            }

            override fun onFailure(call: Call<ArrayList<Payment>?>, t: Throwable) {
                MaterialDialog(requireContext()).show {
                    title(text = "API ERROR")
                    message(text = "Cannot Fetch Payment History")
                    cornerRadius(16f)
                    positiveButton(text = "Yes") { dialog ->
                        dialog.dismiss()
                    }
                }
                processDialog.dismiss()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
