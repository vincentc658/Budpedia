package com.gajikaryawan.gajiq.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.*
import android.widget.EditText
import android.widget.Toast

open class
BaseAppCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun EditText.getValue(): String = text.toString()
    fun EditText.getValueLong(): Long {
        return if (text.isEmpty()) {
            0
        } else {
            text.toString().replace(".", "").replace(" ", "").replace("Rp", "").replace(",", "")
                .toLong()
        }
    }


    fun EditText.isTextNotEmpty(): Boolean {
        if (text.isNullOrEmpty()) {
            return false
        }
        return true
    }

    fun View.hideView() {
        visibility = View.GONE
    }

    fun View.showView() {
        visibility = View.VISIBLE
    }

    fun View.inVisibleView() {
        visibility = View.INVISIBLE
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun hideViewWithAnimation(view: View) {
        val animate = TranslateAnimation(
            0f,
            view.width.toFloat(),
            0f,
            0f
        )
        animate.duration = 400
        view.startAnimation(animate)
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                animate.fillAfter = false
                view.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
                animate.fillAfter = true
            }

        })
    }

    fun showViewWithAnimation(view: View) {
        view.bringToFront()
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            view.width.toFloat(),
            0f,
            0f,
            0f
        )
        animate.duration = 400
        animate.fillAfter = true
        view.startAnimation(animate)
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {
                view.visibility = View.INVISIBLE
            }

        })
    }


}