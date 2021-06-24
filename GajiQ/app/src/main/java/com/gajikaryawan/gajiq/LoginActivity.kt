package com.gajikaryawan.gajiq

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gajikaryawan.gajiq.database.DatabaseController
import com.gajikaryawan.gajiq.util.BaseAppCompatActivity
import com.gajikaryawan.gajiq.util.SessionManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : BaseAppCompatActivity() {
    private val databaseController = DatabaseController(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_Register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        btn_login.setOnClickListener {
            if (etEmail.text.isNotEmpty() && Password.text.isNotEmpty()){
                databaseController.getAdmin(etEmail.text.toString(), Password.text.toString()){
                    if(it!=null){
                        SessionManager.saveId(it.id.toString())
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        return@getAdmin
                    }else{
                        showToast("Email dan password tidak cocok")
                    }
                }
            }else{
                showToast("Mohon masukkan email dan password")
            }
        }


    }


}