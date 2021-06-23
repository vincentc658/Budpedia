package com.gajikaryawan.gajiq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gajikaryawan.gajiq.database.DatabaseController
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }


    private fun checking():Boolean{
        if(Name.text.toString().trim{it<=' '}.isNotEmpty()
            && Phone.text.toString().trim{it<=' '}.isNotEmpty()
            && EmailRegister.text.toString().trim{it<=' '}.isNotEmpty()
            && PasswordRegister.text.toString().trim{it<=' '}.isNotEmpty()
        )
        {
            return true
        }
        return false
    }
}