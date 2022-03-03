package com.bill.payment.glpays.Network

import com.bill.payment.glpays.Model.*
import com.bill.payment.glpays.Pojo.*
import retrofit2.Call
import retrofit2.http.*
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Multipart

interface ApiInterface {

    @POST("Login")
     fun PostLogin(@Body user: LoginModel): Call<LoginResponse>

    @POST("Registration")
    fun PostSignUp(@Body user: RegisterationModel): Call<RegistrationResponse>

    @POST("Logout")
    fun LogOut(@Body user: LogoutModel): Call<LogoutResponse>

    @POST("ForgetPass")
    fun ForgetPassword(@Body user: ForgetPasswordModel): Call<ForgetPasswordResponse>

    @POST("GetUserDetail")
    fun getUserDetails(@Body user: UserDetailsModel): Call<UserDetailsResponse>

    @POST("UpdateProfile")
    fun UserUpdateProfile(@Body user: UpdateProfileModel): Call<UpdateProfileResponse>

    @POST("UpdateBank")
    fun AddBank(@Body user: AddBankModel): Call<AddBankDetailsResponse>


   // @Multipart
   /* @POST("UpdateProfilePic")
    fun UploadProfilePic(@Part("txtuserID") id: RequestBody,
                         @Part ("userfile") image: RequestBody

    ): Call<UpdateprofilePicPojo>*/

    @Multipart
    @POST("UpdateProfilePic")
    fun UploadProfilePic(
        @Part("txtuserID") description: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Call<UpdateprofilePicPojo>



    @POST("ChangePass")
    fun ChangePass(@Body user: ChangePassModel): Call<ChangePasswordResponse>

    @POST("App_service")
    fun getDashboardData(@Body user: DashBoardModel): Call<DashBoardPageResponse>

// for operator
    @POST("App_operator")
    fun getOperatorData(@Body user: OperatorModel): Call<OperatorResponse>

    // plan currenlt running
    @POST("App_plan")
    fun getplanData(@Body user: PlanModel): Call<PlanResponse>

    // recharge api
    @POST("App_recharge")
    fun getRecharge(@Body user: AppRechargeModel): Call<AppRechargePojo>


}