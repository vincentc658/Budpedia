package com.gajikaryawan.gajiq

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gajikaryawan.gajiq.adapter.DetailSalaryAdapter
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityDetailSalaryBinding
import com.gajikaryawan.gajiq.model.Staff
import com.gajikaryawan.gajiq.util.Constants
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_PAID
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_UNPAID

class DetailSalaryActivity : AppCompatActivity() {
    private val databaseController = DatabaseController(this)
    private lateinit var binding: ActivityDetailSalaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSalaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title= "Detail Gaji"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.rv.layoutManager = LinearLayoutManager(this)
        val staff = intent.getSerializableExtra("staff") as Staff
        var transactionType = PAYMENT_ROLL_UNPAID
        intent.getStringExtra("transactionType")?.let {
            transactionType = it
        }
        if(transactionType== Constants.PAYMENT_ROLL_PAID){
            supportActionBar?.title= "Histori Detail Gaji"
        }
        binding.rv.adapter = DetailSalaryAdapter(
            this,
            databaseController.getPaymentRoll(staff.id.toString(), transactionType),
            staff, transactionType
        )

    }
}