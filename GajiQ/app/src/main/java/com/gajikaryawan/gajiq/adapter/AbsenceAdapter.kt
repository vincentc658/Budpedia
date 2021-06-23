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
import com.gajikaryawan.gajiq.util.Constants.Companion.ABSENCE_IS_ATTEND
import com.gajikaryawan.gajiq.util.Constants.Companion.ABSENCE_IS_PAID
import com.gajikaryawan.gajiq.util.RefreshAdapter

class AbsenceAdapter(
    private val context: Context,
    private val data: ArrayList<Absence>,
    private val callbackAdapter: RefreshAdapter
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val databaseController: DatabaseController by lazy { DatabaseController(context) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.rv_item_absence_staff, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val absence = data[position]
            holder.tvName.text = absence.staffName
            if (absence.isAttend) {
                holder.rgPaid.visibility = View.GONE
                holder.tvIn.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.background_grey_corner_16_border_green
                )
                holder.tvOut.background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_corner_16)
            } else {
                holder.rgPaid.visibility = View.VISIBLE
                holder.tvIn.background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_corner_16)
                holder.tvOut.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.bacgkround_grey_corner_16_border_red
                )
            }
            holder.rbPaid.isChecked = absence.isPaid
            holder.rbNotPaid.isChecked = !absence.isPaid
            holder.tvTypeStaff.setText(if (absence.isPerMonth) "Karyawan Perbulan" else "Karyawan Perhari")

            holder.rbPaid.setOnCheckedChangeListener { _, i ->
                if (i) {
                    val cv = ContentValues()
                    cv.put(ABSENCE_IS_PAID, true)
                    databaseController.updateStatusAbsenceStaff(absence.id.toString(), cv) {
                        callbackAdapter.refresh()
                    }
                }
            }
            holder.rbNotPaid.setOnCheckedChangeListener { _, i ->
                if (i) {
                    val cv = ContentValues()
                    cv.put(ABSENCE_IS_PAID, false)
                    databaseController.updateStatusAbsenceStaff(absence.id.toString(), cv) {
                        callbackAdapter.refresh()
                    }
                }
            }
            holder.tvIn.setOnClickListener {
                val cv = ContentValues()
                cv.put(ABSENCE_IS_ATTEND, true)
                cv.put(ABSENCE_IS_PAID, true)
                databaseController.updateStatusAbsenceStaff(absence.id.toString(), cv) {
                    callbackAdapter.refresh()
                }
                holder.rgPaid.visibility = View.GONE
                holder.tvIn.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.background_grey_corner_16_border_green
                )
                holder.tvOut.background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_corner_16)

            }
            holder.tvOut.setOnClickListener {
                val cv = ContentValues()
                cv.put(ABSENCE_IS_ATTEND, false)
                databaseController.updateStatusAbsenceStaff(absence.id.toString(), cv) {
                    callbackAdapter.refresh()
                }
                holder.rgPaid.visibility = View.VISIBLE
                holder.tvIn.background =
                    ContextCompat.getDrawable(context, R.drawable.background_grey_corner_16)
                holder.tvOut.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.bacgkround_grey_corner_16_border_red
                )
                holder.rbPaid.isChecked = absence.isPaid
                holder.rbNotPaid.isChecked = !absence.isPaid
            }
        }
    }

    override fun getItemCount(): Int = data.size
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName = view.findViewById<TextView>(R.id.tvName)
        var tvIn = view.findViewById<TextView>(R.id.tvIn)
        var tvOut = view.findViewById<TextView>(R.id.tvOut)
        var tvTypeStaff = view.findViewById<TextView>(R.id.tvTypeStaff)
        var rgPaid = view.findViewById<RadioGroup>(R.id.rgPaid)
        var rbNotPaid = view.findViewById<RadioButton>(R.id.rbNotPaid)
        var rbPaid = view.findViewById<RadioButton>(R.id.rbPaid)
    }
}