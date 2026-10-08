package com.setu.salonlocatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.setu.salonlocatorapp.models.SalonModel
class AddEditActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var locationInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()
    }

    private fun createUserInterface() {

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
        val saveButton = Button(this).apply {
            text = "Save"

            setOnClickListener {
                saveSalon()
            }
        }

        val cancelButton = Button(this).apply {
            text = "Cancel"

            setOnClickListener {
                finish()
            }
        }

        root.addView(nameInput)
        root.addView(locationInput)
        root.addView(saveButton)
        root.addView(cancelButton)

        setContentView(root)
    }
        private fun saveSalon() {
            val name = nameInput.text.toString().trim()
            val location = locationInput.text.toString().trim()

            if (name.isEmpty()) {
                nameInput.error = "Salon name is required"
                return
            }

            if (location.isEmpty()) {
                locationInput.error = "Location is required"
                return
            }

            val salon = SalonModel(
                name = name,
                location = location
            )

            AppData.salons.create(salon)

            Toast.makeText(
                this,
                "Salon created",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
