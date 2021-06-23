package com.gajikaryawan.gajiq

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gajikaryawan.gajiq.adapter.WorkerAdapter
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityAddWorkerBinding

class StaffActivity : AppCompatActivity() {
   private  val databaseController = DatabaseController(this)

    private lateinit var binding : ActivityAddWorkerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.rvWorker.layoutManager = LinearLayoutManager(this)
        binding.rvWorker.adapter = WorkerAdapter(this,databaseController.getStaff() )
        binding.cvAddWorker.setOnClickListener {
            startActivity(Intent(this, AddStaffActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvWorker.adapter = WorkerAdapter(this,databaseController.getStaff() )
    }
}
