package com.gajikaryawan.gajiq

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gajikaryawan.gajiq.adapter.AbsencePaySlipAdapter
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityPaySlipBinding
import com.gajikaryawan.gajiq.model.Staff
import com.gajikaryawan.gajiq.util.BaseAppCompatActivity
import java.lang.Exception
import java.text.NumberFormat
import java.util.*

class PaySlip : BaseAppCompatActivity() {
    private val databaseController = DatabaseController(this)
    private lateinit var binding: ActivityPaySlipBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaySlipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Detail Gaji"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val id = intent.getIntExtra("id", 0)
        val staff = intent.getSerializableExtra("staff") as Staff
        val paymentRoll = databaseController.getPaymentRollById(id)
        var total = 0f
        if (staff.isPerMonth!!) {
            val perDay = staff.salary!!.toFloat() / 30
            total = perDay * paymentRoll.totalWorkingDay!!
        } else {
            val perDay = staff.salary!!.toFloat() * paymentRoll.totalWorkingDay!!
            total = perDay
        }
        binding.tvPaymentDateRange.text =
            "Periode Pembayaran : ${paymentRoll.startDate}- ${paymentRoll.endDate}"
        binding.tvSalary.text = "Gaji Bersin : IDR ${getPrice(total)}"
        binding.tvTotal.text = "IDR ${getPrice(total)}"
        binding.tvName.text = "Nama : ${staff.name}"
        binding.tvPhoneNumber.text = "Nomor Handphone : ${staff.phone}"
        binding.tvDate.text = if (paymentRoll.paymentDate == null) "-" else paymentRoll.paymentDate
        binding.tvDesc.text =
            "Gaji Pokok\n${paymentRoll.totalWorkingDay} x IDR ${getPrice(staff.salary!!.toFloat())}"
        binding.rvAbsence.layoutManager = LinearLayoutManager(this)
        binding.rvAbsence.adapter = AbsencePaySlipAdapter(this, databaseController.getAbsenceByPaymentRoll(paymentRoll.idPaymentRoll!!.toString()))

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