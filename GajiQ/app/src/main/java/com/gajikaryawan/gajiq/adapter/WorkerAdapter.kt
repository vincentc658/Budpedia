package com.gajikaryawan.gajiq.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gajikaryawan.gajiq.R
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.model.Staff
import java.lang.Exception
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class WorkerAdapter(private val context: Context,private val data: ArrayList<Staff>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private  val databaseController = DatabaseController(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_staff, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.tvName.text = data[position].name
            holder.tvNumber.text = data[position].phone
            holder.tvSalary.text = "IDR ${getPrice(data[position].salary!!.toFloat())}"
            holder.tvType.text = if(data[position].isPerMonth!!)"Perbulan" else "Perhari"
            holder.tvDelete.setOnClickListener {
                databaseController.deleteStaff(data[position].id!!.toString()){status, message->
                    if (status){
                        data.clear()
                        data.addAll(databaseController.getStaff())
                        notifyDataSetChanged()
                    }else{
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    }

                }
            }
        }
    }

    override fun getItemCount(): Int = data.size
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvNumber: TextView = view.findViewById(R.id.tvNumber)
        var tvName: TextView = view.findViewById(R.id.tvName)
        var tvSalary: TextView = view.findViewById(R.id.tvSalary)
        var tvType: TextView = view.findViewById(R.id.tvType)
        var tvDelete: TextView = view.findViewById(R.id.tvDelete)
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
}
