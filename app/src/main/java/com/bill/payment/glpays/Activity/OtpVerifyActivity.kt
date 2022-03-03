package com.bill.payment.glpays.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.bill.payment.glpays.R
import kotlinx.android.synthetic.main.app_bar_otp.*

class OtpVerifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verify)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        initView()
    }

    private fun initView() {
        val mobileno = intent.getStringExtra("Mobile_Number")
        val otp_text: AppCompatTextView = findViewById(R.id.otp_text);

        if (mobileno != null) {
            otp_text.setText(mobileno)
        }


        img_back.setOnClickListener() {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}