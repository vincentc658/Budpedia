package com.gajikaryawan.gajiq.adapter

import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gajikaryawan.gajiq.R
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.model.Absence
import com.gajikaryawan.gajiq.util.Constants

class AbsencePaySlipAdapter (private val context : Context, private val data : ArrayList<Absence>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val databaseController: DatabaseController by lazy { DatabaseController(context) }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ItemViewHolder(
                LayoutInflater.from(context).inflate(R.layout.rv_item_absence, parent, false)
            )

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ItemViewHolder) {
                val absence = data[position]
                holder.tvDate.text = absence.date
                holder.tvAttend.text = if(absence.isAttend)"Hadir" else "Absen"
                holder.tvPaid.text = if(absence.isPaid)"Dibayar" else "Tidak Dibayar"
            }
        }

        override fun getItemCount(): Int = data.size
        inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var tvDate = view.findViewById<TextView>(R.id.tvDate)
            var tvPaid = view.findViewById<TextView>(R.id.tvPaid)
            var tvAttend = view.findViewById<TextView>(R.id.tvAttend)
        }
}