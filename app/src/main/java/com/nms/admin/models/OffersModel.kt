package com.nms.nmsadminapp.models

data class OffersModel(
    val offerId: String,
    val offerName: String,
    val offerDescription: String,
    val offerImage: String,
    val offerPrice: String,
    val offerDiscount: String,
    val offerDiscountPrice: String,
    val offerStartDate: String,
    val offerEndDate: String,
    val offerStatus: String,
    val offerCategory: String,
    val offerBrand: String,
    val offerQuantity: String
)