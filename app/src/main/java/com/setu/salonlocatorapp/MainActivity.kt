package com.setu.salonlocatorapp
import android.content.Intent
import android.widget.Button
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()
    }

    override fun onResume() {
        super.onResume()

        if (::listLayout.isInitialized) {
            displaySalons()
        }
    }

    private fun createUserInterface() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 120, 32, 32)
        }

        val title = TextView(this).apply {
            text = "Salon Locator"
            textSize = 28f
            gravity = Gravity.CENTER
        }


        listLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        val addButton = Button(this).apply {
            text = "Add Salon"
            setOnClickListener {
                val intent = Intent(
                    this@MainActivity,
                    AddEditActivity::class.java
                )

                startActivity(intent)
            }
        }
        root.addView(title)
        root.addView(addButton)
        root.addView(listLayout)

        setContentView(root)
    }

    private fun displaySalons() {

        listLayout.removeAllViews()

        val salons = AppData.salons.findAll()

        if (salons.isEmpty()) {

            val emptyText = TextView(this).apply {
                text = "No salons added yet."
                textSize = 18f
                setPadding(0, 40, 0, 40)
            }

            listLayout.addView(emptyText)
            return
        }

        for (salon in salons) {

            val salonText = TextView(this).apply {
                text =
                    "${salon.name}\n" +
                            "${salon.location}\n" +
                            "Price: €${salon.price}"

                textSize = 18f
                setPadding(0, 20, 0, 20)
            }

            listLayout.addView(salonText)
        }
    }
}