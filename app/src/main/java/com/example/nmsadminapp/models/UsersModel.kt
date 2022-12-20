package com.example.nmsadminapp.models

data class UsersModel(
    val userId: String,
    val userName: String,
    val userMobile: String,
    val userEmail: String,
    val userImage: String,
    val userAddress: String,
    val userPincode: String,
    val userCity: String,
    val userState: String,
    val userCountry: String,
    val userLandmark: String,
    val userAlternateMobile: String,
    val userAddressType: String,
    val userAddressStatus: String,
    val userAddressAddedOn: String,
    val userAddressUpdatedOn: String
)