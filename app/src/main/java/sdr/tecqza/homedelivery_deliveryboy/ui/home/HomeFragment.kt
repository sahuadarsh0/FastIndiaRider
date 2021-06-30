package sdr.tecqza.homedelivery_deliveryboy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import sdr.tecqza.homedelivery_deliveryboy.R
import sdr.tecqza.homedelivery_deliveryboy.api.RiderService
import sdr.tecqza.homedelivery_deliveryboy.databinding.FragmentHomeBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Count
import sdr.tecqza.homedelivery_deliveryboy.model.Response
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var userSharedPreferences: SharedPrefs
    private lateinit var processDialog: ProcessDialog

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        processDialog = ProcessDialog(requireContext())
        userSharedPreferences = SharedPrefs(requireContext(), "USER")

        binding.apply {
            riderName.text = userSharedPreferences["name"]
            riderMobile.text = userSharedPreferences["mobile"]
            checkStatus()
            val imgUrl = RiderService.RIDER_URL + userSharedPreferences["image"]
            Glide.with(requireContext())
                .load(imgUrl)
                .placeholder(R.drawable.fastindia)
                .centerCrop()
                .into(riderImage)
            pendingLayout.setOnClickListener {
                val action = HomeFragmentDirections.actionNavigationHomeToNavigationOrderHistory()
                action.status = "Pending"
                findNavController().navigate(action)
            }
            deliveredLayout.setOnClickListener {
                val action = HomeFragmentDirections.actionNavigationHomeToNavigationOrderHistory()
                action.status = "Delivered"
                findNavController().navigate(action)
            }
            duty.setOnCheckedChangeListener { checked ->
                changeDutyMode()
            }
            bigBazaar.setOnClickListener{
                if (duty.isChecked){
                    findNavController().navigate(R.id.action_navigation_home_to_orderOutletFragment)
                }
                else Toast.makeText(context, "Turn your duty On", Toast.LENGTH_SHORT).show()
            }
        }

        getOrdersCount()
        return root
    }


    private fun changeDutyMode() {
        processDialog.show()
        val changeDutyMode = RiderService.create().updateStatus(userSharedPreferences["mobile"])
        changeDutyMode.enqueue(object : Callback<Response?> {
            override fun onResponse(call: Call<Response?>, response: retrofit2.Response<Response?>) {
                val body = response.body()
                userSharedPreferences["status"] = body?.data?.status
                processDialog.dismiss()
            }
            override fun onFailure(call: Call<Response?>, t: Throwable) {
                processDialog.dismiss()
            }
        })
    }

    private fun checkStatus() {
        if (userSharedPreferences["status"].equals("Active"))
            binding.duty.setChecked(true)
        else
            binding.duty.setChecked(false)
    }

    private fun getOrdersCount() {
        val orderCount = RiderService.create().orderCount(userSharedPreferences["riderId"])
        orderCount.enqueue(object : Callback<Count?> {
            override fun onResponse(call: Call<Count?>, response: retrofit2.Response<Count?>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    binding.apply {
                        pending.text = count?.pending
                        delivered.text = count?.delivered
                    }
                }
            }

            override fun onFailure(call: Call<Count?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}