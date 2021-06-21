package com.gajikaryawan.gajiq

import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gajikaryawan.gajiq.adapter.AbsenceAdapter
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityAbsenceBinding
import com.gajikaryawan.gajiq.util.RefreshAdapter
import java.util.*

class AbsenceActivity : AppCompatActivity(), RefreshAdapter {
    private lateinit var databaseController: DatabaseController
    private lateinit var binding : ActivityAbsenceBinding
    private var date: String?= null
    override fun refresh() {
        if(date!= null){
            binding.rvList.adapter = AbsenceAdapter(this, databaseController.getAbsencePerDay(date!!), this)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAbsenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        databaseController= DatabaseController(this)
        binding.rvList.layoutManager = LinearLayoutManager(this)
        val cal = Calendar.getInstance()
        date = "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH)+1}-${cal.get(Calendar.DATE)}"
        binding.rvList.adapter = AbsenceAdapter(this, databaseController.getAbsencePerDay(date!!), this)
        binding.cv.setOnDateChangeListener { calendarView, i, i2, i3 ->
            date ="$i-${i2+1}-$i3"
            binding.rvList.adapter = AbsenceAdapter(this, databaseController.getAbsencePerDay(date!!), this)

        }
    }
}