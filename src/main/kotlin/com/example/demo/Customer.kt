package com.example.demo

/**
 * A simple data class representing a customer.
 *
 * @property id The unique identifier of the customer.
 * @property name The first name of the customer.
 * @property lastName The last name of the customer.
 */
data class Customer(
    val id: Long,
    val name: String,
    val lastName: String
)