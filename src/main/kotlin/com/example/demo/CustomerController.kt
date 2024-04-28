package com.example.demo

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomerController(val customerService: CustomerService) {
    @PostMapping("/customers")
    fun insert(@RequestBody request: CustomerRequest): String {
        customerService.insertCustomer(
            firstName = request.firstName,
            lastName = request.lastName
        )
        return """
            {
                "status": "success"
            }
        """.trimIndent()
    }

    @GetMapping("/customers")
    fun get(): List<Customer> {
        return customerService.getCustomers()
    }

    @PutMapping("/customers/{id}")
    fun update(@PathVariable("id") id: Int, @RequestBody request: CustomerRequest): String {
        customerService.updateCustomer(
            id = id,
            firstName = request.firstName,
            lastName = request.lastName
        )
        return """
            {
                "status": "success"
            }
        """.trimIndent()
    }

    @DeleteMapping("/customers/{id}")
    fun delete(@PathVariable("id") id: Int): String {
        customerService.deleteCustomer(id)
        return """
            {
                "status": "success"
            }
        """.trimIndent()
    }
}

data class CustomerRequest(
    val firstName: String,
    val lastName: String
)
