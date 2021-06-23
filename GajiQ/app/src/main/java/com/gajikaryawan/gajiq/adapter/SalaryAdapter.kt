package com.gajikaryawan.gajiq.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.gajikaryawan.gajiq.DetailSalaryActivity
import com.gajikaryawan.gajiq.R
import com.gajikaryawan.gajiq.model.Staff
import java.lang.Exception
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SalaryAdapter(private val context: Context, private val data: ArrayList<Staff>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_sallary_staff, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.tvName.text = data[position].name
            holder.tvPrice.text = "IDR ${getPrice(data[position].totalPay!!)}"
            holder.cvParent.setOnClickListener {
                val intent =Intent(context, DetailSalaryActivity::class.java)
                intent.putExtra("staff", data[position])
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = data.size
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvPrice: TextView = view.findViewById(R.id.tvPrice)
        var tvName: TextView = view.findViewById(R.id.tvName)
        var cvParent: CardView = view.findViewById(R.id.cvParent)
    }
   private fun getPrice(price : Float): String {
       return try {

           val locale = Locale("in")
           val nf = NumberFormat.getNumberInstance(locale)



           nf.format(price)
       } catch (e: Exception) {
           price.toString()
       }
   }
}