package com.bill.payment.glpays.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.bill.payment.glpays.Model.ForgetPasswordModel
import com.bill.payment.glpays.Model.PayglXXX
import com.bill.payment.glpays.Network.ApiClient
import com.bill.payment.glpays.Pojo.ForgetPasswordResponse
import com.bill.payment.glpays.R
import kotlinx.android.synthetic.main.activity_forgot.*

import retrofit2.Call
import retrofit2.Response

class ForgotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }*/

        initView()
    }

    private fun initView() {
        val btn_continue:AppCompatButton = findViewById(R.id.btn_continue)
        btn_continue.setOnClickListener(){
            checkValidation()
        }


        img_back.setOnClickListener() {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun checkValidation() {
        val etphone:AppCompatEditText = findViewById(R.id.etphone)

        if (etphone.text.toString().trim().isEmpty()) {
            etphone.setError("Please Enter Valid UserId");
            etphone.requestFocus();
        }  else {
            CallForgetPasswordApi()
        }
    }

    private fun CallForgetPasswordApi() {
        val pDialog = ProgressDialog(this@ForgotActivity)
        pDialog.setMessage(applicationContext?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.ForgetPassword(ForgetPasswordModel(PayglXXX(etphone.text.toString())))

        apiInterface.enqueue(object : retrofit2.Callback<ForgetPasswordResponse> {

            override fun onResponse(call: Call<ForgetPasswordResponse>, response: Response<ForgetPasswordResponse>) {

                if (response.isSuccessful) {
                    Log.e("Login Response", "" + response.body()?.Paygl?.response)
                    Toast.makeText(applicationContext,response.body()?.Paygl?.response, Toast.LENGTH_SHORT).show()
                    pDialog.dismiss()
                } else {
                    Toast.makeText(applicationContext,response.body()?.Paygl?.response, Toast.LENGTH_SHORT).show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("LoginResponseFail", "" + t)
                pDialog.dismiss()
            }
        })

    }
}