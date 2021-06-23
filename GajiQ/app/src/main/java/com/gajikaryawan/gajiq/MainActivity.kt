package com.gajikaryawan.gajiq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityMainBinding
import com.gajikaryawan.gajiq.util.BaseAppCompatActivity
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_PAID
import com.gajikaryawan.gajiq.util.TimeConverter
import java.util.*

class MainActivity : BaseAppCompatActivity() {
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
        binding.cvReport.setOnClickListener {
            val intent = Intent(this, SalaryStaff::class.java)
            intent.putExtra("transactionType", PAYMENT_ROLL_PAID)
            startActivity(intent)
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
                val cal = Calendar.getInstance()
                cal[Calendar.YEAR] = cal.get(Calendar.YEAR)
                cal[Calendar.MONTH] = cal.get(Calendar.MONTH)
                val numDays = cal.getActualMaximum(Calendar.DATE)

                val startDate = "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-01"
                val endDate = "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-$numDays"
                databaseController.insertPaymentRoll(it.id!!.toLong(), startDate, endDate) { id, status, message ->
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
}