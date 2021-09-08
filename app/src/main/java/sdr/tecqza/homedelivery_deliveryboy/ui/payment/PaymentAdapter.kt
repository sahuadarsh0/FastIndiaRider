package sdr.tecqza.homedelivery_deliveryboy.ui.payment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.list.customListAdapter
import sdr.tecqza.homedelivery_deliveryboy.R
import sdr.tecqza.homedelivery_deliveryboy.databinding.ListPaymentBinding
import sdr.tecqza.homedelivery_deliveryboy.databinding.PaymentDetailsBinding
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
                card.setOnClickListener {
                    openDialog(payment)
                }
                monthCard.setOnClickListener {
                    openDialog(payment)
                }
            }
        }

        private fun openDialog(payment: Payment) {
            Log.d("asa", "openDialog:$payment")
            val dialog = MaterialDialog(itemView.context).show {
                title(text = payment.month)
                cornerRadius(16f)
                customView(R.layout.payment_details, scrollable = true, horizontalPadding = true)
                positiveButton(text = "OK") { dialog ->
                    dialog.dismiss()
                }
            }
            val customView = dialog.getCustomView()
            val da: TextView = customView.findViewById(R.id.da)
            val hra: TextView = customView.findViewById(R.id.hra)
            val lta: TextView = customView.findViewById(R.id.lta)
            val bonus: TextView = customView.findViewById(R.id.bonus)
            val specialAllowance: TextView = customView.findViewById(R.id.special_allowance)
            val otherAllowance: TextView = customView.findViewById(R.id.other_allowance)
            val pf: TextView = customView.findViewById(R.id.pf)
            val esi: TextView = customView.findViewById(R.id.esi)
            val other: TextView = customView.findViewById(R.id.other)
            val basicPay: TextView = customView.findViewById(R.id.basic_pay)
            val netSalary: TextView = customView.findViewById(R.id.net_salary)

            da.text = payment.da
            hra.text = payment.hra
            lta.text = payment.lta
            bonus.text = payment.bonus
            specialAllowance.text = payment.specialAllowance
            otherAllowance.text = payment.otherAllowance
            pf.text = payment.pf
            esi.text = payment.esi
            other.text = payment.other
            basicPay.text = payment.basicPay
            netSalary.text = payment.netSalary
        }

    }
}
