package com.gajikaryawan.gajiq

import android.os.Bundle
import android.util.Log
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityAddStaffBinding
import com.gajikaryawan.gajiq.util.BaseAppCompatActivity
import java.util.*

class AddStaffActivity : BaseAppCompatActivity() {
    private val databaseController: DatabaseController by lazy { DatabaseController(this) }
    private lateinit var binding: ActivityAddStaffBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val cal = Calendar.getInstance()

        binding.cvAddWorker.setOnClickListener {
            if (!binding.etNama.isTextNotEmpty()) {
                showToast("Masukkan Nama staff")
                return@setOnClickListener
            }
            if (!binding.etNumber.isTextNotEmpty()) {
                showToast("Masukkan Nomor telefon staff")
                return@setOnClickListener
            }
            if (!binding.etSalary.isTextNotEmpty()) {
                showToast("Masukkan Gaji staff")
                return@setOnClickListener
            }
            databaseController.insertData(
                binding.etNama.text.toString(), binding.rbMonth.isChecked,
                binding.etSalary.text.toString().toInt(),
                binding.etNumber.text.toString()
            ) { id, status, message ->
                if (status) {
                    binding.etNama.text.clear()
                    binding.etNumber.text.clear()
                    binding.etSalary.text.clear()
                    addPaymentRoll(id)
                } else {
                    if (message != null) {
                        showToast(message)
                        return@insertData
                    }
                    showToast("Terjadi masalah")
                }
            }
        }
    }

    private fun addPaymentRoll(id: Long) {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = cal.get(Calendar.YEAR)
        cal[Calendar.MONTH] = cal.get(Calendar.MONTH)
        val numDays = cal.getActualMaximum(Calendar.DATE)

        if (!databaseController.getPaymentRol(
                id,
                "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-01"
            )
        ) {
            val startDate = "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-01"
            val endDate = "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-$numDays"
            databaseController.insertPaymentRoll(id, startDate, endDate) { id, status, message ->
                if (status) {
                    for(i in 1 until cal.get(Calendar.DATE)+1){
                        databaseController.insertAbsence(id,"${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-$i"){status, message ->}
                    }
                    finish()
                } else {
                    if (message != null) {
                        showToast(message)
                        return@insertPaymentRoll
                    }
                    showToast("Terjadi masalah Payment Roll")
                }
            }
        }
    }
}