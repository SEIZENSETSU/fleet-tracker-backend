package com.example.demo

import org.springframework.stereotype.Service

interface CustomerService {
    fun insertCustomer(firstName: String, lastName: String)
    fun getCustomers(): List<Customer>
    fun updateCustomer(id: Int, firstName: String, lastName: String)
    fun deleteCustomer(id: Int)
}

@Service
class CustomerServiceImpl(val customerRepository: CustomerRepository) : CustomerService {
    override fun insertCustomer(firstName: String, lastName: String) {
        customerRepository.add(
            firstName = firstName,
            lastName = lastName
        )
        return
    }

    override fun getCustomers(): List<Customer> {
        return customerRepository.find()
    }

    override fun updateCustomer(id: Int, firstName: String, lastName: String) {
        customerRepository.update(
            id = id,
            firstName = firstName,
            lastName = lastName
        )
        return
    }

    override fun deleteCustomer(id: Int) {
        customerRepository.delete(id)
        return
    }
}
