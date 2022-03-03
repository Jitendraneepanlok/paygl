package com.bill.payment.glpays.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bill.payment.glpays.Adapter.IntroSliderAdapter
import com.bill.payment.glpays.Fragments.*
import com.bill.payment.glpays.Helper.ManagePermissions
import com.bill.payment.glpays.R
import kotlinx.android.synthetic.main.activity_welcome_slider.*
import java.util.*

class WelcomeSliderActivity : AppCompatActivity() {
    private val fragmentList = ArrayList<Fragment>()
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 5000 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 7000 // time in milliseconds between successive task executions.
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_slider)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

        // Initialize a list of required permissions to request runtime
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALENDAR
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)

        AutoViewPager()
        InitView()
    }

    private fun AutoViewPager() {
        val adapter = IntroSliderAdapter(this)
        vpIntroSlider.adapter = adapter
        fragmentList.addAll(listOf(Intro1Fragment(), Intro2Fragment(), Intro3Fragment(), Intro4Fragment(), Intro5Fragment()))
        adapter.setFragmentList(fragmentList)
        indicatorLayout.setIndicatorCount(adapter.itemCount)
        indicatorLayout.selectCurrentPosition(0)

        /*After setting the adapter use the timer */
        val handler = Handler()
        val Update = Runnable {
            val NUM_PAGES = 0;
            if (currentPage === NUM_PAGES - 1) {
                currentPage = 0
            }
            vpIntroSlider.setCurrentItem(currentPage++, true)
        }

        timer = Timer()

        timer!!.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
        registerListeners()
    }

    private fun registerListeners() {
        vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                indicatorLayout.selectCurrentPosition(position)
                if (position < fragmentList.lastIndex) {
                    Toast.makeText(applicationContext, "last", Toast.LENGTH_SHORT)
                } else {

                }
            }
        })

    }

    private fun InitView() {
        btn_register.setOnClickListener() {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btn_login.setOnClickListener() {
            //startActivity(Intent(this, LoginActivity::class.java))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                managePermissions.checkPermissions()
        }
        }

    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)

                if (isPermissionsGranted) {
                    // Do the task now
                    toast("Permissions granted.")
                    startActivity(Intent(this@WelcomeSliderActivity, LoginActivity::class.java))
                } else {
                    toast("Permissions denied.")
                }
                return
            }
        }
    }
}


// Extension function to show toast message
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    startActivity(Intent(applicationContext, LoginActivity::class.java))

}