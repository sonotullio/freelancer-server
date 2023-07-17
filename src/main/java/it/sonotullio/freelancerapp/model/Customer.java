package it.sonotullio.freelancerapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

        @Id
        @GeneratedValue(generator="system-uuid")
        @GenericGenerator(name="system-uuid", strategy = "uuid2")
        private String id;
        @Column(unique = true, nullable = false)
        private String email;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private String surname;
        private String phone;
        private String address;
        @Column(unique = true, nullable = false)
        private String fiscalCode;
        @JsonIgnore
        @OneToMany(mappedBy="customer", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Project> projects;
}
