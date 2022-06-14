package com.bill.payment.glpays.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bill.payment.glpays.Model.PayglX
import com.bill.payment.glpays.Model.RegisterationModel
import com.bill.payment.glpays.Network.ApiClient
import com.bill.payment.glpays.Pojo.RegistrationResponse
import com.bill.payment.glpays.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btn_continue
import kotlinx.android.synthetic.main.activity_register.et_name
import kotlinx.android.synthetic.main.app_bar_register.*
import kotlinx.android.synthetic.main.app_bar_register.img_back
import retrofit2.Call
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

        initView()
    }

    private fun initView() {

        img_back.setOnClickListener(){
            startActivity(Intent(this, WelcomeSliderActivity::class.java))

        }
        btn_continue.setOnClickListener(){
//            startActivity(Intent(this, OtpVerifyActivity::class.java))
            checkValidation()
        }
    }

    private fun checkValidation() {

        val et_name :AppCompatEditText = findViewById(R.id.et_name)
        val et_email : AppCompatEditText = findViewById(R.id.et_email)
        val etphone : AppCompatEditText = findViewById(R.id.etphone);


        if (et_name.text.toString().trim().isEmpty()) {
            et_name.setError("Please Enter Valid UserName");
            et_name.requestFocus();
        } else if (et_email.text.toString().trim().isEmpty()) {
            et_email.setError("Please Enter Valid Email");
            et_email.requestFocus();
        } else if (etphone.text.toString().trim().isEmpty()){
            etphone.setError("Please Enter your Valid Mobile No.")
            etphone.requestFocus();
        }else{
            CallSignUpApi()

        }
    }

    private fun CallSignUpApi() {
        val pDialog = ProgressDialog(this@RegisterActivity)
        pDialog.setMessage(application?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.PostSignUp(
            RegisterationModel(
                PayglX("",
                    et_email.text.toString(),
                    etphone.text.toString(),
                    et_name.text.toString(),
                    "GLX123456",
                    "",
                    "1")
            )
        )

        apiInterface.enqueue(object : retrofit2.Callback<RegistrationResponse> {

            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.GLPAYS?.response)
                    Toast.makeText(applicationContext, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT).show()
                    callnewPage(response.body()?.GLPAYS?.txtmobile.toString())
                    pDialog.dismiss()
                } else {
                    Toast.makeText(applicationContext, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT).show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    private fun callnewPage(txtmobile: String) {

        val intent = Intent(this, OtpVerifyActivity::class.java)
        intent.putExtra("Mobile_Number",txtmobile)
        startActivity(intent)

    }
}