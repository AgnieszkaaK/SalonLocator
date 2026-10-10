package com.setu.salonlocatorapp

import com.setu.salonlocatorapp.models.SalonModel

interface OnSalonListener {

    fun onSalonClick(salon: SalonModel)

    fun onSalonLongClick(salon: SalonModel): Boolean
}