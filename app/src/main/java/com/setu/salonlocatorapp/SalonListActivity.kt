package com.setu.salonlocatorapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.setu.salonlocatorapp.models.SalonModel

class SalonListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SalonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_salon_list)

        recyclerView =
            findViewById(R.id.SalonRecyclerView)

        adapter = SalonAdapter(
            salons = AppData.salons.findAll(),

            onEdit = { salon ->
                editSalon(salon)
            },

            onDelete = { salon ->
                deleteSalon(salon)
            }
        )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        val returnButton =
            findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            adapter.updateSalons(
                AppData.salons.findAll()
            )
        }
    }

    private fun editSalon(salon: SalonModel) {

        val intent =
            Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", salon.id)

        startActivity(intent)
    }

    private fun deleteSalon(salon: SalonModel) {

        AppData.salons.delete(salon.id)

        adapter.updateSalons(
            AppData.salons.findAll()
        )
    }
}