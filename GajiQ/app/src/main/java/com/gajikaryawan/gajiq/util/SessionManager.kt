package com.gajikaryawan.gajiq.util

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences

    private val PREFERENCESNAME by lazy { "gaji" }

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCESNAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveId(id: String) {
        editor.putString("id", id)
        editor.putBoolean("isLogin", true)
        editor.commit()
    }
    val id: String get() = sharedPreferences.getString("id", "")!!
    val isLogin: Boolean get() = sharedPreferences.getBoolean("isLogin", false)!!

}
