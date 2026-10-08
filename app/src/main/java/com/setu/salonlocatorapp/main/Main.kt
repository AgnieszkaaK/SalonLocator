package com.setu.salonlocatorapp.main

import com.setu.salonlocatorapp.models.SalonMemStore

val store = SalonMemStore()

fun main() {
    println("=== Salon Locator Console App (Lab 1) ===")

    var input: Int

    do {
        input = menu()

        when (input) {
            1 -> println("Add salon — coming next")
            2 -> listSalons()
            3 -> println("Update salon — coming next")
            4 -> println("Delete salon — coming next")
            5 -> println("Search salon — coming next")
            0 -> println("\nExiting Salon Locator. Goodbye!")
            else -> println("\nInvalid option. Please try again.")
        }
    } while (input != 0)
}

fun menu(): Int {
    println("\n----------------------------------")
    println(" MAIN MENU")
    println("----------------------------------")
    println(" 1. Add Salon")
    println(" 2. List All Salons")
    println(" 3. Update a Salon")
    println(" 4. Delete a Salon")
    println(" 5. Search Salon by ID")
    println(" 0. Exit")
    print("\nEnter option: ")

    return readlnOrNull()?.toIntOrNull() ?: -1
}

fun listSalons() {
    println("\n--- All Salons ---")

    val salons = store.findAll()

    if (salons.isEmpty()) {
        println("No salons stored yet.")
    } else {
        salons.forEach {
            println(
                "ID: ${it.id} | Name: ${it.name} | " +
                        "Location: ${it.location} | Price: ${it.price}"
            )
        }
    }
}