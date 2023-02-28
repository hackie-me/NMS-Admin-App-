package com.example.nmsadminapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nmsadminapp.LoginActivity
import com.example.nmsadminapp.R
import com.example.nmsadminapp.UpdateAdminProfileActivity
import com.example.nmsadminapp.utils.Helper
import de.hdodenhof.circleimageview.CircleImageView

class AccountFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvEditProfile: TextView
    private lateinit var tvLogout: TextView
    private lateinit var ivProfilePicture: CircleImageView
    private lateinit var tvContactRequest: TextView
    private lateinit var tvTermsAndConditions: TextView
    private lateinit var tvPrivacyPolicy: TextView
    private lateinit var tvAboutUs: TextView
    private lateinit var tvFAQ: TextView
    private lateinit var adminName: String
    private lateinit var adminEmail: String
    private lateinit var adminPhone: String
    private lateinit var adminProfile: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize values
        init()

        /*----- set Click listeners -----*/
        // Edit profile
        tvEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), UpdateAdminProfileActivity::class.java)
            intent.putExtra("name", adminName)
            intent.putExtra("email", adminEmail)
            intent.putExtra("phone", adminPhone)
            intent.putExtra("picture", adminProfile)
            startActivity(intent)
        }

        // Contact Request
        tvContactRequest.setOnClickListener {
            Helper.showSnackBar(requireView(), "Contact Request")
        }
        // About us
        tvAboutUs.setOnClickListener {
            Helper.showSnackBar(requireView(), "About Us")
        }
        // Terms and Conditions
        tvTermsAndConditions.setOnClickListener {
            Helper.showSnackBar(requireView(), "Terms and Conditions")
        }
        // Privacy Policy
        tvPrivacyPolicy.setOnClickListener {
            Helper.showSnackBar(requireView(), "Privacy Policy")
        }
        // FAQ
        tvFAQ.setOnClickListener {
            // Open FAQ fragment
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, FaqFragment())
                ?.commit()
        }
        // logout
        tvLogout.setOnClickListener {
            Helper.removeSharedPreference(requireContext(), "token")
            Helper.showToast(requireContext(), "Logged out successfully")
            startActivity(Intent(requireContext(), LoginActivity::class.java), null)
        }
    }

    override fun onResume() {
        super.onResume()
         // convert json string to json object
        adminName = Helper.getDataFromToken(requireContext(), "name")!!
        adminProfile = Helper.getDataFromToken(requireContext(), "image")!!
        // if admin profile is blank then set default profile picture
        if (adminProfile == "") {
            adminProfile = "https://i.pravatar.cc/150"
        }
        adminEmail = Helper.getDataFromToken(requireContext(), "email")!!
        adminPhone = Helper.getDataFromToken(requireContext(), "phone")!!

        // set admin name
        tvName.text = "Hi, $adminName"

        // set Admin profile picture
        Glide.with(requireContext())
            .load(adminProfile)
            .into(ivProfilePicture)
    }

    // Function to Initialize values
    private fun init() {
        tvName = view?.findViewById(R.id.tvName)!!
        tvEditProfile = view?.findViewById(R.id.tvEditProfile)!!
        tvLogout = view?.findViewById(R.id.tvManageLogout)!!
        ivProfilePicture = view?.findViewById(R.id.ivProfile)!!
        tvContactRequest = view?.findViewById(R.id.tvManageContactRequests)!!
        tvTermsAndConditions = view?.findViewById(R.id.tvManageTerms)!!
        tvPrivacyPolicy = view?.findViewById(R.id.tvManagePrivacy)!!
        tvAboutUs = view?.findViewById(R.id.tvManageAboutUs)!!
        tvFAQ = view?.findViewById(R.id.tvManageFAQ)!!
    }
}