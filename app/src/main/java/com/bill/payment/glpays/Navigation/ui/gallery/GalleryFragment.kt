package com.bill.payment.glpays.Navigation.ui.gallery

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bill.payment.glpays.Helper.SessionManager
import com.bill.payment.glpays.Model.PayglXXXX
import com.bill.payment.glpays.Model.PayglXXXXX
import com.bill.payment.glpays.Model.UpdateProfileModel
import com.bill.payment.glpays.Model.UserDetailsModel
import com.bill.payment.glpays.Network.ApiClient
import com.bill.payment.glpays.Pojo.UpdateProfileResponse
import com.bill.payment.glpays.Pojo.UpdateprofilePicPojo
import com.bill.payment.glpays.Pojo.UserDetailsResponse
import com.bill.payment.glpays.R
import com.bill.payment.glpays.databinding.FragmentGalleryBinding
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*
import okhttp3.MultipartBody


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val GALLERY = 1
    private val CAMERA = 2
    // This property is only valid between onCreateView and
    // onDestroyView.
    lateinit var sessionManager: SessionManager
    lateinit var user_id: String

    private lateinit var textUserName: AppCompatTextView
    private lateinit var textEmail: AppCompatTextView
    private lateinit var spingender: AppCompatSpinner

    private lateinit var etdob: AppCompatEditText
    private lateinit var etname: AppCompatEditText
    private lateinit var etmobile: AppCompatEditText
    private lateinit var etemail: AppCompatEditText
    private lateinit var btnupdate : AppCompatButton
    private lateinit var Gender : String

    private lateinit var addimg : AppCompatImageView
    private lateinit var accountprofile : CircleImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sessionManager = SessionManager(activity)

        user_id = sessionManager.getUserData(SessionManager.User_Id).toString()
        Log.e("Value", "" + user_id)
        textUserName = binding.txtUserName
        textEmail = binding.txtUserEmail

        /*galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        getUserDetailsApi()
        initView()

        return root
    }

    private fun initView() {
        // access the items of the spinner
        val languages = resources.getStringArray(R.array.Gender)

        // access the spinner
        spingender = binding.spingender

        if (spingender != null) {
            val adapter =
                activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, languages) }
            spingender.adapter = adapter

            spingender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                     Gender = spingender.getSelectedItem().toString();
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

            etname = binding.etname
            etmobile = binding.etmobile
            etemail = binding.etemail

            etdob = binding.etdob
            etdob.setOnClickListener() {
                CallCalender()
            }

            btnupdate = binding.btnupdate
            btnupdate.setOnClickListener(){
                checkValidation()
            }

            accountprofile = binding.accountprofile
            addimg = binding.addimg
            addimg.setOnClickListener(){
                OpenDialog()
            }

        }
    }

    private fun OpenDialog() {
        val pictureDialog = activity?.let { AlertDialog.Builder(it) }
        pictureDialog?.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera", "Cancel")
        pictureDialog?.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
                2 -> dialog.cancel()
            }
        }
        pictureDialog?.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /*if (resultCode == this.RESULT_CANCELED)
       {
       return
       }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    val galleryuri = Uri.parse(path)

                    accountprofile!!.setImageBitmap(bitmap)
                    UpdateProfilePic(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            accountprofile!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            Log.e("ImagePathfromCamera", "" + thumbnail)
            UpdateProfilePic(thumbnail)
        }
    }

    private fun UpdateProfilePic(imageUri: Bitmap) {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(activity?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

       /* val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()

        val fname: String = "jituuuu"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        file.createNewFile()
        try {
            val out = FileOutputStream(file)
            imageUri.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
      //  val file = File(imageUri!!.path)
        val fbody = RequestBody.create(MediaType.parse("image/*"), file)
        val name: RequestBody = RequestBody.create(MediaType.parse("text/plain"),user_id)*/
        */

        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()

        val fname: String = "jituuuu"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        file.createNewFile()
        try {
            val out = FileOutputStream(file)
            imageUri.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val fbody: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

      /*  val file: File = FileUtils.getFile(this, fileUri)
        val requestFile = RequestBody.create(
            MediaType.parse(getContentResolver().getType(fileUri)),
            file
        )*/

        val body = MultipartBody.Part.createFormData("userfile", file.name, fbody)
        val descriptionString = user_id
        val description = RequestBody.create(MultipartBody.FORM, descriptionString)

        val apiInterface = ApiClient.getClient.UploadProfilePic(description,body)

        apiInterface.enqueue(object : retrofit2.Callback<UpdateprofilePicPojo> {
            override fun onResponse(call: Call<UpdateprofilePicPojo>, response: Response<UpdateprofilePicPojo>) {
                if (response.isSuccessful) {
                    Log.e("PickResponse", "" + response.body()?.GLPAYS?.response)
                    Toast.makeText(activity, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT).show()
                    pDialog.dismiss()
                } else {
                    Toast.makeText(activity, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT).show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateprofilePicPojo>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            Log.e("heel", "" + wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()

            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                activity,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"),
                null
            )
            fo.close()
            Log.e("", "File Saved" + f.getAbsolutePath())

            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/paygl"
    }

    @SuppressLint("UseRequireInsteadOfGet")

    private fun CallCalender() {
        val mcurrentDate = Calendar.getInstance()

        var mDay = mcurrentDate[Calendar.DAY_OF_MONTH]
        var mMonth = mcurrentDate[Calendar.MONTH]
        var mYear = mcurrentDate[Calendar.YEAR]
        val datePickerDialog = activity?.let {
            DatePickerDialog(
                it,
                { view, year, month, dayOfMonth ->
                    var month = month
                    month = month + 1
//                    var date = "$dayOfMonth-$month-$year"
                    var date = "$year-$month-$dayOfMonth"
                    etdob.setText(date)
                }, mYear, mMonth, mDay
            )
        }
        datePickerDialog?.show()

    }

    private fun getUserDetailsApi() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(activity?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface =
            ApiClient.getClient.getUserDetails(UserDetailsModel(PayglXXXX(user_id/*"1"*/)))

        apiInterface.enqueue(object : retrofit2.Callback<UserDetailsResponse> {

            override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>
            ) {

                if (response.isSuccessful) {
                    Log.e("Login Response", "" + response.body()?.GLPAYS?.response)
                    Toast.makeText(activity, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT)
                        .show()

                    if (response.body()?.GLPAYS?.txtname != null) {
                        textUserName.setText("Hi "+response.body()?.GLPAYS?.txtname)
                        etname.setText(response.body()?.GLPAYS?.txtname)

                    }
                    if (response.body()?.GLPAYS?.txtemail != null) {
                        textEmail.setText(response.body()?.GLPAYS?.txtemail)
                        etemail.setText(response.body()?.GLPAYS?.txtemail)
                    }
                    if (response.body()?.GLPAYS?.txtdob != null){
                        etdob.setText(response.body()?.GLPAYS?.txtdob)
                    }

                    if (response.body()?.GLPAYS?.txtmobile!=null){
                        etmobile.setText(response.body()?.GLPAYS?.txtmobile)
                    }

                    pDialog.dismiss()

                    /*  val img_user = viewOfLayout.findViewById<CircleImageView>(R.id.img_user)

                      val media = response.body()?.GLPAYS?.txtphoto
                      if (media !== null) {
                          activity?.let {
                              Glide.with(it)
                                  .load(media)
                                  .into(img_user)
                          }
                      } else {
                          img_user.setImageResource(R.drawable.user)
                          pDialog.dismiss()
                      }*/

                } else {
                    Toast.makeText(activity, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("LoginResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkValidation() {
        if (etname.text.toString().trim().isEmpty()) {
            etname.setError("Please Enter Valid UserName");
            etname.requestFocus();
        } else if (etemail.text.toString().trim().isEmpty()) {
            etemail.setError("Please Enter Valid Email");
            etemail.requestFocus();
        } else if (etdob.text.toString().trim().isEmpty()) {
            etdob.setError("Please Enter your Date of Brithday")
            etdob.requestFocus();
        } else if (spingender.getSelectedItem().toString().trim().equals("Select")) {
            Toast.makeText(activity, "Please select your gender", Toast.LENGTH_SHORT).show();
            spingender.requestFocus();
        } else {
            CallUpdateProfileApi()
        }
    }

    private fun CallUpdateProfileApi() {
        val pDialog = ProgressDialog(activity)
        pDialog.setMessage(activity?.getString(R.string.dialog_msg));
        pDialog.setCancelable(false);
        pDialog.show();

        val apiInterface = ApiClient.getClient.UserUpdateProfile(
            UpdateProfileModel(
                PayglXXXXX("", etdob.text.toString(),
                    etemail.text.toString(),
                    spingender.getSelectedItem().toString(),
                    etname.text.toString(),
                    "",
                    user_id/*"1"*/
                )
            )
        )

        apiInterface.enqueue(object : Callback<UpdateProfileResponse> {

            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                if (response.isSuccessful) {
                    Log.e("Response", "" + response.body()?.GLPAYS?.response)
                    Toast.makeText(activity, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                } else {
                    Toast.makeText(activity, response.body()?.GLPAYS?.response, Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                Toast.makeText(activity, "" + t, Toast.LENGTH_SHORT).show()
                Log.e("ResponseFail", "" + t)
                pDialog.dismiss()
            }
        })
    }
}