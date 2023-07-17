package it.sonotullio.freelancerapp.repository;

import it.sonotullio.freelancerapp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    Iterable<Project> findAllByCustomerEmail(String customerEmail);
    Iterable<Project> findAllByCustomerId(String customerId);

}
