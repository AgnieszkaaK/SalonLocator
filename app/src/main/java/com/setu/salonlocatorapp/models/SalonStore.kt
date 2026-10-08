package com.setu.salonlocatorapp.models

interface SalonStore {
    fun findAll(): List<SalonModel>

    fun create(salon: SalonModel)

    fun update(salon: SalonModel): Boolean

    fun delete(id: Long): Boolean

    fun findOne(id: Long): SalonModel?
}