package sdr.tecqza.homedelivery_deliveryboy.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import sdr.tecqza.homedelivery_deliveryboy.databinding.FragmentOrderHistoryBinding

class OrderHistoryFragment : Fragment() {

    private lateinit var orderHistoryViewModel: OrderHistoryViewModel
    private var _binding: FragmentOrderHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        orderHistoryViewModel =
            ViewModelProvider(this).get(OrderHistoryViewModel::class.java)

        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        orderHistoryViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}