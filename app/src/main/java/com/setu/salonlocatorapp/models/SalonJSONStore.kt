package com.setu.salonlocatorapp.models

import java.util.concurrent.atomic.AtomicLong
import timber.log.Timber
import android.content.Context
import java.io.File
import com.google.gson.Gson
//based on SalonMemStore

class SalonJSONStore(context: Context) : SalonStore {

    private val gson = Gson()// converts salon objects to JSON and back


    // the file used to save salons in the app's internal storage
    private val file = File(context.filesDir, "salons.json")
//filesDir gives folder for app to save its private files
    private val salons = ArrayList<SalonModel>()
    private val lastId = AtomicLong(0L)

    init { // runs when the store is created and loads any saved salons
        load()
    }

    override fun findAll(): List<SalonModel> {
        return salons
    }

    override fun create(salon: SalonModel) { //when salon gets added, the counter is increased by 1 and assigns the new ID to the salon
        salon.id = lastId.incrementAndGet()
        salons.add(salon) //add the salon to the in-memory state
        save()

        Timber.d("JSON store: created salon id=${salon.id}")
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
            save()
            Timber.d("Memory store: updated salon id=${salon.id}")
            true
        } else {
            false
        }
    }

    override fun delete(id: Long): Boolean {
        val foundSalon = findOne(id)

        return if (foundSalon != null) {
            salons.remove(foundSalon)
            save()
            Timber.d("Memory store: deleted salon id=$id")
            true
        } else {
            false
        }
    }
    private fun save() {

        // converts the current salon list into JSON text
        val json = gson.toJson(salons)

        // writes the text to salons.json, replacing its previous contents
        file.writeText(json)
    }
    private fun load() {

        // there is no saved file when the app is first used
        if (!file.exists()) {
            return
        }

        // reads the JSON text from the file
        val json = file.readText()

        // converts the JSON text back into salon objects
        val savedSalons = gson.fromJson(json, Array<SalonModel>::class.java)

        // puts the saved salons into our list
        salons.addAll(savedSalons)

        // finds the highest saved ID so new salons get a different ID
        for (salon in salons) {
            if (salon.id > lastId.get()) {
                lastId.set(salon.id)
            }
        }
    }


    override fun findOne(id: Long): SalonModel? {
        return salons.find { salon -> salon.id == id }

    }

}