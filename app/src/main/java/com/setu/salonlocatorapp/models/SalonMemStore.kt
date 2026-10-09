package com.setu.salonlocatorapp.models

import java.util.concurrent.atomic.AtomicLong

class SalonMemStore : SalonStore {

    private val salons = ArrayList<SalonModel>()
    private val lastId = AtomicLong(0L)

    override fun findAll(): List<SalonModel> {
        return salons
    }

    override fun create(salon: SalonModel) { //when salon gets added, the counter is increased by 1 and assigns the new ID to the salon
        salon.id = lastId.incrementAndGet()
        salons.add(salon) //add the salon to the in-memory state
    }

    override fun update(salon: SalonModel): Boolean {
        val foundSalon = findOne(salon.id)

        return if (foundSalon != null) {
            foundSalon.name = salon.name
            foundSalon.location = salon.location
            foundSalon.price = salon.price
            foundSalon.distanceKm = salon.distanceKm
            foundSalon.visited = salon.visited
            foundSalon.personalRating = salon.personalRating
            foundSalon.personalReview = salon.personalReview
            true
        } else {
            false
        }
    }

    override fun delete(id: Long): Boolean {
        val foundSalon = findOne(id)

        return if (foundSalon != null) {
            salons.remove(foundSalon)
            true
        } else {
            false
        }
    }

    override fun findOne(id: Long): SalonModel? {
        return salons.find { salon -> salon.id == id }
    }
}