package com.setu.salonlocatorapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val viewSalonsButton =
            findViewById<Button>(R.id.viewSalonsButton)

        val addSalonButton =
            findViewById<Button>(R.id.addSalonButton)

        viewSalonsButton.setOnClickListener {
            startActivity(
                Intent(this, SalonListActivity::class.java)
            )
        }

        addSalonButton.setOnClickListener {
            startActivity(
                Intent(this, AddEditActivity::class.java)
            )
        }
    }
}