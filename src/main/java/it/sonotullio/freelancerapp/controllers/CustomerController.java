package it.sonotullio.freelancerapp.controllers;

import it.sonotullio.freelancerapp.exceptions.NotFoundException;
import it.sonotullio.freelancerapp.model.Customer;
import it.sonotullio.freelancerapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping
    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable String id) {
        return customerRepository.findById(id).orElseThrow( () -> new NotFoundException("Customer not found") );
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable String id, @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        customerRepository.deleteById(id);
    }
}
