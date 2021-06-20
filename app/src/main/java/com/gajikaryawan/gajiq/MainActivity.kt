package com.gajikaryawan.gajiq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityMainBinding
import com.gajikaryawan.gajiq.util.TimeConverter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseController: DatabaseController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        databaseController = DatabaseController(this)
        binding.rlStaff.setOnClickListener {
            startActivity(Intent(this, StaffActivity::class.java))
        }
        binding.cvAbsence.setOnClickListener {
            startActivity(Intent(this, AbsenceActivity::class.java))
        }
        binding.cvSallary.setOnClickListener {
            startActivity(Intent(this, SalaryStaff::class.java))

        }
        absenceStaff()
    }

    private fun absenceStaff() {
        val todayDate = Calendar.getInstance().get(Calendar.DATE)
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)+1
        val date = "${
            Calendar.getInstance().get(Calendar.YEAR)
        }-$thisMonth-01"
        databaseController.getStaff().forEach {
            val idPaymentRoll=databaseController.getPaymentRollId(date, it.id.toString())
            if(idPaymentRoll!=0){
                val lastDate =TimeConverter.convertDate(
                    databaseController.getLastAbsence(idPaymentRoll.toString()),
                    TimeConverter.YYYY_MM_DD,
                    TimeConverter.DD
                )
                val cal = Calendar.getInstance()
                for(i in lastDate.toInt()+1 until todayDate+1){
                    databaseController.insertAbsence(idPaymentRoll.toLong(),"${cal.get(Calendar.YEAR)}-$thisMonth-$i"){status, message ->
                        Log.d("next absence "," $status")
                    }
                }
            }else{

            }
        }
    }
}