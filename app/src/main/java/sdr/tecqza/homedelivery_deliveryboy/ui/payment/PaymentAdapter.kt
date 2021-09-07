package sdr.tecqza.homedelivery_deliveryboy.ui.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sdr.tecqza.homedelivery_deliveryboy.databinding.ListPaymentBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Payment
import technited.minds.androidutils.ProcessDialog

class PaymentAdapter : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    private val paymentList = ArrayList<Payment>()
    private lateinit var processDialog: ProcessDialog

    fun setList(payment: ArrayList<Payment>?) {
        paymentList.clear()
        paymentList.addAll(payment!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentAdapter.PaymentViewHolder {
        val binding = ListPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        processDialog = ProcessDialog(parent.context)
        return PaymentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: PaymentAdapter.PaymentViewHolder, position: Int) {
        holder.bind(paymentList[position])
    }

    inner class PaymentViewHolder(private val binding: ListPaymentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(payment: Payment) {
            binding.apply {
                month.text = payment.month
                netSalary.text = "Rs. ${payment.netSalary}"
            }
        }

    }
}
