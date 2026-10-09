package com.setu.salonlocatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.setu.salonlocatorapp.models.SalonModel
import android.text.InputType
class AddEditActivity : AppCompatActivity() {
//fields initialised when interface is created
    private lateinit var nameInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var priceInput: EditText
    private lateinit var distanceInput: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //saves screen states

        createUserInterface()
    }

    private fun createUserInterface() { // arranges form controls vertically with space around edges

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 120, 32, 32)
        }

        nameInput = EditText(this).apply {
            hint = "Salon name"
        }

        locationInput = EditText(this).apply {
            hint = "Location"
        }
        priceInput = EditText(this).apply {
            hint = "Price (€)"
            inputType =
                InputType.TYPE_CLASS_NUMBER or //requests numeric input (with possible decimal) for price
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        distanceInput = EditText(this).apply {
            hint = "Distance (km)" // distance is entered in kilometers
            inputType =
                InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        val saveButton = Button(this).apply { //validates the form and saves when button is tapped
            text = "Save"

            setOnClickListener {
                saveSalon()
            }
        }

        val cancelButton = Button(this).apply { //closes screen without saving
            text = "Cancel"

            setOnClickListener {
                finish()
            }
        }
//controls in the order they should appear
        root.addView(nameInput)
        root.addView(locationInput)
        root.addView(priceInput)
        root.addView(distanceInput)
        root.addView(saveButton)
        root.addView(cancelButton)

        setContentView(root)
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

            val salon = SalonModel(
                name = name,
                location = location,
                price = price,
                distanceKm = distance
            )

            AppData.salons.create(salon) //add salon to shared in-memory store

            Toast.makeText( //confirm success and return to the previous screen
                this,
                "Salon created",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
