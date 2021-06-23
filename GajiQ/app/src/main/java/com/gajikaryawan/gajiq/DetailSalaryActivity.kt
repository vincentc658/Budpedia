package com.gajikaryawan.gajiq

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gajikaryawan.gajiq.adapter.DetailSalaryAdapter
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityDetailSalaryBinding
import com.gajikaryawan.gajiq.model.Staff

class DetailSalaryActivity: AppCompatActivity() {
    private val databaseController = DatabaseController(this)
    private lateinit var binding : ActivityDetailSalaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailSalaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.rv.layoutManager = LinearLayoutManager(this)
        val staff = intent.getSerializableExtra("staff") as Staff
        binding.rv.adapter = DetailSalaryAdapter(this, databaseController.getPaymentRoll(staff.id.toString()), staff)

    }
}