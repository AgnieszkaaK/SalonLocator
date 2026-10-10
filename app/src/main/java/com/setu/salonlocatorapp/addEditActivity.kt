package com.setu.salonlocatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.setu.salonlocatorapp.models.SalonModel
import timber.log.Timber
class AddEditActivity : AppCompatActivity() {

    //fields initialised when interface is created
    private lateinit var nameInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var priceInput: EditText
    private lateinit var distanceInput: EditText
    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()

        // if ID is missing, it means adding new salon
        editingId = intent.getLongExtra("id", -1L)

        if (editingId != -1L) {
            findViewById<TextView>(R.id.formTitle).text = "Edit Salon"
            loadExistingSalon(editingId!!)
        }
    }

    private fun createUserInterface() {

        // layout settings are not defined in activityaddedit.xml
        setContentView(R.layout.activityaddedit)

        nameInput = findViewById(R.id.nameInput)
        locationInput = findViewById(R.id.locationInput)

        // XML inputType setting
        //requests numeric input (with possible decimal) for price
        priceInput = findViewById(R.id.priceInput)

        // distance is entered in kilometers
        distanceInput = findViewById(R.id.distanceInput)

        val saveButton = findViewById<Button>(R.id.saveButton)

        //validates the form and saves when button is tapped
        saveButton.setOnClickListener {
            saveSalon()
        }

        val cancelButton = findViewById<Button>(R.id.cancelButton)

        //closes screen without saving
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun loadExistingSalon(id: Long) {

        val salon = AppData.salons.findOne(id)

        if (salon == null) {
            Toast.makeText(
                this,
                "Salon not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        nameInput.setText(salon.name)
        locationInput.setText(salon.location)
        priceInput.setText(salon.price.toString())
        distanceInput.setText(salon.distanceKm.toString())
    }

    private fun saveSalon() { //reads text and removes any spaces from start or end
        val name = nameInput.text.toString().trim()
        val location = locationInput.text.toString().trim()

        if (name.isEmpty()) {
            nameInput.error = "Salon name is required" //show error is nothing entered
            return
        }

        if (location.isEmpty()) {
            locationInput.error = "Location is required"
            return
        }

        val price = priceInput.text.toString().toDoubleOrNull() // converts price to double

        if (price == null || !price.isFinite() || price < 0) { // checks that the number is not infinity or not a number or invalid numbers
            priceInput.error = "Enter a valid price of 0 or more"
            return
        }

        val distance = distanceInput.text.toString().toDoubleOrNull()

        if (distance == null || !distance.isFinite() || distance < 0) {
            distanceInput.error = "Enter a valid distance of 0 or more"
            return
        }

        if (editingId == null || editingId == -1L) {

            val salon = SalonModel(
                name = name,
                location = location,
                price = price,
                distanceKm = distance
            )

            AppData.salons.create(salon) //add salon to shared in-memory store
            Timber.d("Created salon id=${salon.id}")

            Toast.makeText( //confirm success and return to the previous screen
                this,
                "Salon created",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            val existingSalon = AppData.salons.findOne(editingId!!) // finds the saved salon so editing its details keeps its visited status, rating and review

            if (existingSalon == null) {
                Toast.makeText(
                    this,
                    "Salon not found",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
                return
            }

            // keeps the same ID and preserves fields not edited by this form
            val salon = SalonModel(
                id = editingId!!,
                name = name,
                location = location,
                price = price,
                distanceKm = distance,
                visited = existingSalon.visited,
                personalRating = existingSalon.personalRating,
                personalReview = existingSalon.personalReview
            )

            AppData.salons.update(salon)
            Timber.d("Updated salon id=${salon.id}")

            Toast.makeText(
                this,
                "Salon updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}