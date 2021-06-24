package com.gajikaryawan.gajiq

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.databinding.ActivityRegisterBinding
import com.gajikaryawan.gajiq.util.BaseAppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseAppCompatActivity() {
    private val databaseController = DatabaseController(this)
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.Continue.setOnClickListener {
            if (checking()) {
                databaseController.insertDataAdmin(
                    Name.text.toString(),
                    Phone.text.toString(),
                    EmailRegister.text.toString(),
                    PasswordRegister.text.toString()
                ) { id, status, message ->
                    if (status) {
                        finish()
                    }

                }
            }
        }
    }


    private fun checking(): Boolean {
        if (Name.text.toString().trim { it <= ' ' }.isNotEmpty()
            && Phone.text.toString().trim { it <= ' ' }.isNotEmpty()
            && EmailRegister.text.toString().trim { it <= ' ' }.isNotEmpty()
            && PasswordRegister.text.toString().trim { it <= ' ' }.isNotEmpty()
        ) {
            return true
        }
        showToast("Mohon isi data lengkap")
        return false
    }
}