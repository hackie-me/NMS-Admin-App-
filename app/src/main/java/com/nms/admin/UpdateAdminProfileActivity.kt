package com.nms.admin

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nms.admin.models.AdminModel
import com.nms.admin.repo.AdminRepository
import com.nms.admin.service.Authentication
import com.nms.admin.utils.Helper
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateAdminProfileActivity : AppCompatActivity() {

    private lateinit var etEditProfileName: EditText
    private lateinit var etEditProfileEmail: EditText
    private lateinit var etEditProfilePhone: EditText
    private lateinit var etEditProfilePicture: CircleImageView
    private lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_admin_profile)

        // initialize values
        init()

        // get data from intent
        val intent = intent
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val phone = intent.getStringExtra("phone")
        val picture = intent.getStringExtra("picture")

        // set data to views
        etEditProfileName.setText(name)
        etEditProfileEmail.setText(email)
        etEditProfilePhone.setText(phone)

        Glide.with(this)
            .load(picture)
            .into(etEditProfilePicture)

        btnUpdate.setOnClickListener {

            val imageBase64 = Helper.convertImageToBase64(etEditProfilePicture)

            val adminModel = AdminModel(
                adminName = etEditProfileName.text.toString(),
                adminEmail = etEditProfileEmail.text.toString(),
                adminPhone = etEditProfilePhone.text.toString(),
                adminImage = imageBase64
            )

            CoroutineScope(Dispatchers.IO).launch {
                val response = AdminRepository.update(
                    adminModel,
                    Authentication.getToken(this@UpdateAdminProfileActivity)!!
                )
                withContext(Dispatchers.Main) {
                    if (response.code == 204) {
                        Helper.showToast(this@UpdateAdminProfileActivity, "Profile Updated")
                        // Add new token to shared preferences
                        Authentication.storeToken(
                            this@UpdateAdminProfileActivity,
                            response.data!!
                        )
                        finish()
                    } else {
                        Helper.showToast(
                            this@UpdateAdminProfileActivity,
                            response.code.toString()
                        )
                        Helper.showToast(this@UpdateAdminProfileActivity, "Error Updating Profile")
                    }
                }
            }
        }

        // Edit Profile Image
        etEditProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
            Helper.showToast(this, "Image selected")
        }
    }

    // get profile picture
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            etEditProfilePicture.setImageURI(imageUri)
        }
    }

    // Init Function
    private fun init() {
        etEditProfileName = findViewById(R.id.etEditAdminName)
        etEditProfileEmail = findViewById(R.id.etEditEmail)
        etEditProfilePhone = findViewById(R.id.etEditMobile)
        etEditProfilePicture = findViewById(R.id.ivEditProfileImage)
        btnUpdate = findViewById(R.id.btnUpdateAdminProfile)
    }

}