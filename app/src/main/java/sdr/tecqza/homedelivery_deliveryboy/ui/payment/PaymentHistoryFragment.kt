package sdr.tecqza.homedelivery_deliveryboy.ui.payment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.datetime.selectedDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sdr.tecqza.homedelivery_deliveryboy.R
import sdr.tecqza.homedelivery_deliveryboy.api.RiderService
import sdr.tecqza.homedelivery_deliveryboy.databinding.FragmentOrderHistoryBinding
import sdr.tecqza.homedelivery_deliveryboy.databinding.FragmentPaymentHistoryBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Order
import sdr.tecqza.homedelivery_deliveryboy.model.Payment
import sdr.tecqza.homedelivery_deliveryboy.ui.order.OrderAdapter
import sdr.tecqza.homedelivery_deliveryboy.ui.order.OrderHistoryFragmentArgs
import sdr.tecqza.homedelivery_deliveryboy.ui.order.OrderViewModel
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*

class PaymentHistoryFragment : Fragment() {

    private var _binding: FragmentPaymentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var userSharedPreferences: SharedPrefs
    private lateinit var processDialog: ProcessDialog
    private var payments: ArrayList<Payment>? = null
    private var startCalender: LocalDate? = null
    private var endCalender: LocalDate? = null
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

        binding.paymentsList.layoutManager = GridLayoutManager(context,2)
        binding.paymentsList.adapter = adapter
        processDialog.show()

        getAllPayment()

         binding.advanced.setOnClickListener {
            startC()
        }
        return root
    }

    private fun startC() {
        MaterialDialog(requireContext()).show {
            title(text = "Select Start Date")
            cornerRadius(16f)
            datePicker(maxDate = Calendar.getInstance()) { dialog, date ->
                Toast.makeText(context, ("Selected Start date: ${selectedDate().formatDate()}"), Toast.LENGTH_SHORT).show()
                dialog.setOnDismissListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startCalender = LocalDate.parse(selectedDate().formatDate())
                    } else Toast.makeText(context, "Not Supported", Toast.LENGTH_SHORT).show()

                }
            }
        }
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


    @RequiresApi(Build.VERSION_CODES.O)
    fun filter(type: String) {
        val filteredList = arrayListOf<Payment>()
        val Today = LocalDate.now()
        val Formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy")
      when (type) {
            "Today" -> {
                val currentDate = Today.format(Formatter)
                payments?.let {
                    for (item in it) {
                        if (item.date!!.contains(currentDate)) {
                            filteredList.add(item)
                        }
                    }
                }
            }

            "Yesterday" -> {
                val yesterday0 = Today.minusDays(1)
                Log.d("asa", "filter: yesterday $yesterday0")
                val yesterday = yesterday0.format(Formatter)
                Log.d("asa", "filter: formattedYesterday $yesterday")
                payments?.let {
                    for (item in it) {
                        if (item.date!!.contains(yesterday.toString())) {
                            filteredList.add(item)
                        }
                    }
                }
            }
            "Mtd" -> {

                val first = Today.with(TemporalAdjusters.firstDayOfMonth())
                Log.d("asa", "filter: first $first")
                val firstDayOfMonth = first.format(Formatter)
                Log.d("asa", "filter: firstDayOfMonth $firstDayOfMonth")
                val ranges = first..Today
                payments?.let {
                    for (item in it) {
                        if (LocalDate.parse(item.date!!, DateTimeFormatter.ofPattern("dd/MMM/yyyy")) in ranges) {
                            filteredList.add(item)
                        }
                    }
                }
            }
            "Range" -> {

                val first = startCalender
                val last = endCalender
                Log.d("asa", "filter: first $first")
                Log.d("asa", "filter: last $last")
                val firstDay = first?.format(Formatter)
                val lastDay = last?.format(Formatter)
                Log.d("asa", "filter: firstDay $firstDay")
                Log.d("asa", "filter: lastDay $lastDay")
                val ranges = first!!..last!!
                payments?.let {
                    for (item in it) {
                        if (LocalDate.parse(item.date!!, DateTimeFormatter.ofPattern("dd/MMM/yyyy")) in ranges) {
                            filteredList.add(item)
                        }
                    }
                }
            }
        }
        adapter.setList(filteredList)
        adapter.notifyDataSetChanged()

    }

    private fun Calendar.formatDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(this.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
