package com.setu.salonlocatorapp.models

data class SalonModel(
    var id: Long = 0L,
    var name: String = "",
    var location: String = "",
    var price: Double = 0.0,
    var distanceKm: Double = 0.0,
    var visited: Boolean = false,
    var personalRating: Int = 0,
    var personalReview: String = ""
)