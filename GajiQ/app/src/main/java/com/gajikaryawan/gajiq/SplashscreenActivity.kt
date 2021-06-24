package com.gajikaryawan.gajiq

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.gajikaryawan.gajiq.util.SessionManager

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        Handler(Looper.getMainLooper()).postDelayed({
            if(SessionManager.isLogin) {

                startActivity(Intent(this@SplashscreenActivity, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashscreenActivity, LoginActivity::class.java))
                finish()
            }
        }, 1000)
    }
}