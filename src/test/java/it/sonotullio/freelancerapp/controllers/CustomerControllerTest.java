package it.sonotullio.freelancerapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sonotullio.freelancerapp.model.Customer;
import it.sonotullio.freelancerapp.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerRepository customerRepository;

    public static Customer getCustomer() {
        return Customer.builder()
                .id("1")
                .email("tforneris94@gmail.com")
                .name("Tullio")
                .surname("Forneris")
                .phone("1234567890")
                .address("Via Roma 1")
                .build();
    }

    private static Stream<Customer> getInvalidCustomers() {
        var customer = getCustomer();

        return Stream.of(
                customer.toBuilder().email(null).build(),
                customer.toBuilder().email("").build(),
                customer.toBuilder().name(null).build(),
                customer.toBuilder().name("").build(),
                customer.toBuilder().surname(null).build(),
                customer.toBuilder().surname("").build()
        );
    }

    @Test
    void create() throws Exception {
        // given
        Customer customerToSave = getCustomer();
        customerToSave.setId(null);

        Customer returnedCustomer = getCustomer();

        // when
        when(customerRepository.save(customerToSave)).thenReturn(returnedCustomer);

        mvc.perform(
                post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(customerToSave))
        )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedCustomer.getId())))
                .andExpect(jsonPath("$.email", is(returnedCustomer.getEmail())))
                .andExpect(jsonPath("$.name", is(returnedCustomer.getName())))
                .andExpect(jsonPath("$.surname", is(returnedCustomer.getSurname())))
                .andExpect(jsonPath("$.phone", is(returnedCustomer.getPhone())))
                .andExpect(jsonPath("$.address", is(returnedCustomer.getAddress())));
    }

    @ParameterizedTest
    @MethodSource("getInvalidCustomers")
    void createKo() throws Exception {
        // given
        Stream<Customer> invalidCustomers = getInvalidCustomers();

        // when
        mvc.perform(
                post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(invalidCustomers))
        )
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById() throws Exception {
        // given
        Customer returnedCustomer = getCustomer();

        // when
        when(customerRepository.findById(returnedCustomer.getId())).thenReturn(java.util.Optional.of(returnedCustomer));
        mvc.perform(get("/api/customers/" + returnedCustomer.getId()))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedCustomer.getId())))
                .andExpect(jsonPath("$.email", is(returnedCustomer.getEmail())))
                .andExpect(jsonPath("$.name", is(returnedCustomer.getName())))
                .andExpect(jsonPath("$.surname", is(returnedCustomer.getSurname())))
                .andExpect(jsonPath("$.phone", is(returnedCustomer.getPhone())))
                .andExpect(jsonPath("$.address", is(returnedCustomer.getAddress())));

    }

    @Test
    void findByIdNotFound() throws Exception {
        String id = "1";

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        mvc.perform(get("/api/customers/" + id))
                .andExpect(status().isNotFound());
    }
}