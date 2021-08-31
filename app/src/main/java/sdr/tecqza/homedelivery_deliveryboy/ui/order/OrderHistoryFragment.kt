package sdr.tecqza.homedelivery_deliveryboy.ui.order

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
import sdr.tecqza.homedelivery_deliveryboy.model.Order
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*

class OrderHistoryFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var userSharedPreferences: SharedPrefs
    private lateinit var processDialog: ProcessDialog
    private var orders: ArrayList<Order>? = null
    private var startCalender: LocalDate? = null
    private var endCalender: LocalDate? = null
    val args: OrderHistoryFragmentArgs by navArgs()
    private val adapter = OrderAdapter()

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

        binding.ordersList.layoutManager = LinearLayoutManager(context)
        binding.ordersList.adapter = adapter
        processDialog.show()

        if (args.status != "All") {
            getOrder(args.status)
        } else {
            getAllOrder()
        }

        binding.radioFilter.setOnCheckedChangeListener { group, i ->
            val selected = group.checkedRadioButtonId
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                when (selected) {
                    R.id.today -> filter("Today")
                    R.id.yesterday -> filter("Yesterday")
                    R.id.mtd -> filter("Mtd")
                }
            } else Toast.makeText(context, "Not Supported", Toast.LENGTH_SHORT)
                .show()
        }
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

                    end()
                }
            }
        }
    }

    private fun end() {
        MaterialDialog(requireContext()).show {
            title(text = "Select End Date")
            datePicker(maxDate = Calendar.getInstance()) { dialog, date ->
                Toast.makeText(context, ("Selected End date: ${selectedDate().formatDate()}"), Toast.LENGTH_SHORT).show()
                dialog.setOnDismissListener {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        endCalender =  LocalDate.parse(selectedDate().formatDate())
                        filter("Range")
                    } else Toast.makeText(context, "Not Supported", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun getAllOrder() {
        val order = RiderService.create().index(userSharedPreferences["riderId"])
        order.enqueue(object : Callback<ArrayList<Order>?> {
            override fun onResponse(call: Call<ArrayList<Order>?>, response: Response<ArrayList<Order>?>) {
                orders = response.body()
                adapter.setList(orders)
                adapter.notifyDataSetChanged()
                processDialog.dismiss()
            }

            override fun onFailure(call: Call<ArrayList<Order>?>, t: Throwable) {
                MaterialDialog(requireContext()).show {
                    title(text = "API ERROR")
                    message(text = "Cannot Fetch Order History")
                    cornerRadius(16f)
                    positiveButton(text = "Yes") { dialog ->
                        dialog.dismiss()
                    }
                }
                processDialog.dismiss()
            }
        })
    }

    private fun getOrder(status: String) {
        val order = RiderService.create().order(userSharedPreferences["riderId"], status)
        order.enqueue(object : Callback<ArrayList<Order>?> {
            override fun onResponse(call: Call<ArrayList<Order>?>, response: Response<ArrayList<Order>?>) {
                orders = response.body()
                adapter.setList(orders)
                adapter.notifyDataSetChanged()
                processDialog.dismiss()
            }

            override fun onFailure(call: Call<ArrayList<Order>?>, t: Throwable) {
                MaterialDialog(requireContext()).show {
                    title(text = "API ERROR")
                    message(text = "Cannot Fetch Order History")
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
        val filteredList = arrayListOf<Order>()
        val Today = LocalDate.now()
        val Formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy")
        when (type) {
            "Today" -> {
                Log.d("asa", "filter: today $Today")
                val currentDate = Today.format(Formatter)
                Log.d("asa", "filter: currentDate $currentDate")
                orders?.let {
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
                orders?.let {
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
                orders?.let {
                    for (item in it) {
                        if (LocalDate.parse(item.date!!, DateTimeFormatter.ofPattern("dd/MMM/yyyy")) in ranges) {
                            filteredList.add(item)
                        }
                    }
                }
            }
            "Range" -> {

                val first = startCalender
                Log.d("asa", "filter: first $first")
                val firstDayOfMonth = first?.format(Formatter)
                Log.d("asa", "filter: firstDayOfMonth $firstDayOfMonth")
                val ranges = startCalender!!..endCalender!!
                orders?.let {
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
