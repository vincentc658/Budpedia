package com.gajikaryawan.gajiq.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gajikaryawan.gajiq.PaySlip
import com.gajikaryawan.gajiq.R
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.model.PaymentRoll
import com.gajikaryawan.gajiq.model.Staff
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_PAID
import com.gajikaryawan.gajiq.util.TimeConverter
import java.lang.Exception
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailSalaryAdapter(
    private val context: Context,
    private val data: ArrayList<PaymentRoll>,
    private val staff: Staff,
    private val transactionType: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val databaseController = DatabaseController(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.rv_item_detail_salary, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val paymentRoll = data[position]
            holder.tvDate.text = "${paymentRoll.startDate} - ${paymentRoll.endDate}"
            var total = 0f
            if (staff.isPerMonth!!) {
                val perDay = staff.salary!!.toFloat() / 30
                total = perDay * paymentRoll.totalWorkingDay!!
            } else {
                val perDay = staff.salary!!.toFloat() * paymentRoll.totalWorkingDay!!
                total = perDay
            }
            holder.tvTotal.text = "IDR ${getPrice(total)}"
            holder.tvSubTotal.text = "IDR ${getPrice(total)}"
            holder.tvMonthPerSalary.text =
                "${paymentRoll.totalWorkingDay} x ${getPrice(staff.salary!!.toFloat())}"
            val todayDate = Calendar.getInstance().get(Calendar.DATE)
            val thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
            val date = "${
                Calendar.getInstance().get(Calendar.YEAR)
            }-$thisMonth-$todayDate"
            val month = TimeConverter.convertDate(
                paymentRoll.endDate!!, TimeConverter.YYYY_MM_DD,
                TimeConverter.MM
            )
            if (transactionType == PAYMENT_ROLL_PAID) {
                holder.tvPay.visibility = View.GONE

            } else {
                if (thisMonth > month.toInt()) {
                    holder.tvPay.visibility = View.VISIBLE
                } else {
                    if (date == paymentRoll.endDate) {
                        holder.tvPay.visibility = View.VISIBLE
                    } else {
                        holder.tvPay.visibility = View.GONE
                    }
                }
            }
            holder.tvPay.setOnClickListener {
                val cal = Calendar.getInstance()
                val date =
                    "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DATE)}"
                holder.tvPay.visibility = View.GONE
                databaseController.updatePaymentRollStatus(
                    paymentRoll.idPaymentRoll.toString(),
                    date
                ) {
                    Log.d("updatePaymentRollStatus ", " $it")

                }
            }
            holder.parent.setOnClickListener {
                val intent = Intent(context, PaySlip::class.java)
                intent.putExtra("staff", staff)
                intent.putExtra("id", paymentRoll.idPaymentRoll)
                context.startActivity(intent)
            }
        }
    }

    private fun getPrice(price: Float): String {
        return try {

            val locale = Locale("in")
            val nf = NumberFormat.getNumberInstance(locale)



            nf.format(price)
        } catch (e: Exception) {
            price.toString()
        }
    }

    override fun getItemCount(): Int = data.size
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDate: TextView = view.findViewById(R.id.tvDate)
        var tvTotal: TextView = view.findViewById(R.id.tvTotal)
        var tvSubTotal: TextView = view.findViewById(R.id.tvSubTotal)
        var tvMonthPerSalary: TextView = view.findViewById(R.id.tvMonthPerSalary)
        var tvPay: TextView = view.findViewById(R.id.tvPay)
        var parent: ConstraintLayout = view.findViewById(R.id.parent)
    }


}