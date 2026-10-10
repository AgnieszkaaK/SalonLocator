package com.setu.salonlocatorapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.setu.salonlocatorapp.models.SalonModel
import androidx.appcompat.app.AlertDialog
import timber.log.Timber
import android.widget.CompoundButton //works with switch button

class SalonListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SalonAdapter
    private lateinit var ratingFilterSwitch: CompoundButton //adapter declarations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_salon_list) //loads salon list from XML layout

        recyclerView =
            findViewById(R.id.SalonRecyclerView) //connects the variable to recycler view that displays salon cards

        ratingFilterSwitch =
            findViewById(R.id.ratingFilterSwitch) //connects the variable to the rating filter switch

        // refreshes the list when the switch is turned on or off
        ratingFilterSwitch.setOnCheckedChangeListener { buttonView, isChecked -> //only runs code when the switch is used
            refreshSalons()
        }
        adapter = SalonAdapter(
            salons = AppData.salons.findAll(),

            listener = object : OnSalonListener {

                override fun onSalonClick(salon: SalonModel) {
                    editSalon(salon)
                }

                override fun onSalonLongClick(salon: SalonModel): Boolean {
                    deleteSalon(salon)
                    return true
                }
            }
        )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        val returnButton =
            findViewById<Button>(R.id.returnButton) //finds the back to menu in the layout

        returnButton.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) { //checjk if adapter has been made
            refreshSalons() //updates list using current filkter
        }
    }
    private fun refreshSalons() {

        val salons = AppData.salons.findAll()

        if (ratingFilterSwitch.isChecked) {

            // only displays salons with a personal rating of 4 or 5
            val filteredSalons = salons.filter { salon ->
                salon.personalRating >= 4
            }

            adapter.updateSalons(filteredSalons) //displays the list of filtered salons

        } else {

            // displays all salons when the filter is off
            adapter.updateSalons(salons)
        }
    }

    private fun editSalon(salon: SalonModel) {
        Timber.d("Editing salon id=${salon.id}, name=${salon.name}")

        val intent =
            Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", salon.id)

        startActivity(intent)
    }
    private fun deleteSalon(salon: SalonModel) {
//ai helped me choose alertdialog.builder and explained how the code works

        AlertDialog.Builder(this) // creates the dialog for the activity
            .setTitle("Delete salon?") // sets the question
            .setMessage("Delete ${salon.name}?") //sets the question under with salon name
            .setPositiveButton("Delete") { dialog, button -> // runs the existing delete and refresh code only when delete is tapped
// "dialog, button" are required but not needed by the delete code because the button knows what to do
                AppData.salons.delete(salon.id)

             //   adapter.updateSalons(              previous refreshing screen
               //     AppData.salons.findAll() )
                refreshSalons() //updates list using current filter
            }
            .setNegativeButton("Cancel", null) //cancels dialog without changin anything
            .show() //displays it
    }
}