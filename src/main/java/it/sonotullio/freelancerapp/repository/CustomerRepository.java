package it.sonotullio.freelancerapp.repository;

import it.sonotullio.freelancerapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
    void deleteByEmail(String email);
}
