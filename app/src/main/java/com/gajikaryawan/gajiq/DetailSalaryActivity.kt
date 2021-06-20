package com.gajikaryawan.gajiq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gajikaryawan.gajiq.adapter.DetailSalaryAdapter
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityDetailSalaryBinding

class DetailSalaryActivity: AppCompatActivity() {
    private val databaseController = DatabaseController(this)
    private lateinit var binding : ActivityDetailSalaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailSalaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = DetailSalaryAdapter(this, databaseController.getStaff())
    }
}