package com.nms.admin.models

data class ReviewModel(
    val reviewId: String,
    val reviewProductId: String,
    val reviewUserId: String,
    val reviewUserName: String,
    val reviewUserImage: String,
    val reviewRating: String,
    val reviewTitle: String,
    val reviewDescription: String,
    val reviewDate: String,
    val reviewTime: String,
    val reviewStatus: String
)