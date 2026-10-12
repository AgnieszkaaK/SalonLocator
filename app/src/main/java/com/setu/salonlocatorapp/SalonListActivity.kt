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
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AdapterView


class SalonListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SalonAdapter
    private lateinit var ratingFilterSwitch: CompoundButton //adapter declarations
    private lateinit var sortSpinner: Spinner //dropdown sorting spiinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_salon_list) //loads salon list from XML layout

        recyclerView =
            findViewById(R.id.SalonRecyclerView) //connects the variable to recycler view that displays salon cards

        sortSpinner =
            findViewById(R.id.sortSpinner) //connects variable to the spinner dropdown
        val sortOptions = listOf( //options in the drop down
            "Name: A–Z",
            "Rating: highest first"
        )

        val sortAdapter = ArrayAdapter( //adapater created to display sort options
            this,
            android.R.layout.simple_spinner_item, //androids layout for selected option
            sortOptions
        )
// dropdown layout for the choices shown when tapped
        sortAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        sortSpinner.adapter = sortAdapter

        // Refreshes the salon list when a sorting option is selected.
        sortSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    refreshSalons()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No action needed when nothing is selected.
                }
            }
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

        // Keeps only ratings of 4 or 5 when the filter is enabled.
        val filteredSalons =
            if (ratingFilterSwitch.isChecked) {
                salons.filter { it.personalRating >= 4 }
            } else {
                salons
            }

        // Option 1 sorts by rating; option 0 sorts by name.
        val sortedSalons =
            if (sortSpinner.selectedItemPosition == 1) { //if selected is 1
                filteredSalons.sortedByDescending { it.personalRating } //sort by rating (highest to lowest)
            } else {
                filteredSalons.sortedBy { it.name.lowercase() } //if 0, sort by name
            }

        adapter.updateSalons(sortedSalons) //update the view with new salons
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