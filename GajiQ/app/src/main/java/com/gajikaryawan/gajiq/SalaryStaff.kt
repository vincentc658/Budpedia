package com.gajikaryawan.gajiq

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gajikaryawan.gajiq.adapter.SalaryAdapter
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityAddWorkerBinding
import com.gajikaryawan.gajiq.databinding.ActivitySalaryStaffBinding
import com.gajikaryawan.gajiq.model.Staff
import com.gajikaryawan.gajiq.util.Constants
import java.lang.Exception
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SalaryStaff : AppCompatActivity() {
    private val databaseController = DatabaseController(this)
    private lateinit var binding: ActivitySalaryStaffBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalaryStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title= "Gaji"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        var transactionType = Constants.PAYMENT_ROLL_UNPAID
        intent.getStringExtra("transactionType")?.let {
            transactionType = it
        }
        if(transactionType== Constants.PAYMENT_ROLL_PAID){
            supportActionBar?.title= "Histori Gaji"
            binding.cvAddWorker.visibility= View.GONE
        }
        binding.rvWorker.layoutManager = LinearLayoutManager(this)
        binding.rvWorker.adapter = SalaryAdapter(this, generateDataStaff(transactionType), transactionType)
        binding.cvAddWorker.setOnClickListener {
            startActivity(Intent(this, AddStaffActivity::class.java))
        }
    }

    private fun generateDataStaff(transactionType: String ): ArrayList<Staff> {
        val data = ArrayList<Staff>()
        var totalSalary = 0f
        databaseController.getStaff().forEach {
            val staff = Staff()
            staff.name = it.name
            staff.id = it.id
            staff.isPerMonth = it.isPerMonth
            staff.salary = it.salary
            staff.phone = it.phone
            val totalWorkingDay = databaseController.getTotalWorkingDay(it.id.toString(), transactionType)
            Log.d("total working day ", "$totalWorkingDay")
            if(totalWorkingDay!=0) {
                var total = 0f
                if (it.isPerMonth!!) {
                    val perDay = it.salary!!.toFloat() / 30
                    total = perDay * totalWorkingDay
                } else {
                    val perDay = it.salary!!.toFloat() * totalWorkingDay
                    total = perDay
                }
                staff.totalPay = total
                totalSalary += total
                data.add(staff)
            }
        }
        binding.tvTotal.text ="IDR ${getPrice(totalSalary)}"
        return data

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