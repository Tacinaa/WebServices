package service;

import entity.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerService {

    public List<Customer> findAll(int page, int size) {
        return Customer.findAll()
                .page(page, size)
                .list();
    }


    public Optional<Customer> findById(Long id) {
        return Customer.findByIdOptional(id);
    }

    @Transactional
    public Customer create(Customer customer) {
        customer.persist();
        return customer;
    }

    @Transactional
    public Customer update(Long id, Customer updatedCustomer) {
        Customer customer = Customer.findById(id);
        if (customer == null) {
            return null;
        }

        customer.firstName = updatedCustomer.firstName;
        customer.lastName = updatedCustomer.lastName;
        customer.email = updatedCustomer.email;
        customer.phone = updatedCustomer.phone;
        customer.address = updatedCustomer.address;
        customer.city = updatedCustomer.city;
        customer.zipCode = updatedCustomer.zipCode;
        customer.active = updatedCustomer.active;

        return customer;
    }

    @Transactional
    public boolean delete(Long id) {
        return Customer.deleteById(id);
    }
}