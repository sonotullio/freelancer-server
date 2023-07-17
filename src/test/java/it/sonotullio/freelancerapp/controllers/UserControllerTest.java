package it.sonotullio.freelancerapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sonotullio.freelancerapp.model.User;
import it.sonotullio.freelancerapp.repository.UserRepository;
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
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userService;

    private static User getUser() {
        return User.builder()
                .id("1")
                .email("tforneris94@gmail.com")
                .password("pwd")
                .name("Tullio Forneris")
                .picture("https://mypicture.com")
                .build();
    }

    private static Stream<User> getInvalidUsers() {
        var user = getUser();

        return Stream.of(
                user.toBuilder().email(null).build(),
                user.toBuilder().email("").build(),
                user.toBuilder().name(null).build(),
                user.toBuilder().name("").build()
        );
    }

    @Test
    void create() throws Exception {
        // given
        User userToSave = getUser();
        userToSave.setId(null);
        User returnedUser = getUser();

        // when
        when(userService.save(userToSave)).thenReturn(returnedUser);

        mvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userToSave))
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUser.getId())))
                .andExpect(jsonPath("$.email", is(returnedUser.getEmail())))
                .andExpect(jsonPath("$.password", is(returnedUser.getPassword())))
                .andExpect(jsonPath("$.name", is(returnedUser.getName())))
                .andExpect(jsonPath("$.picture", is(returnedUser.getPicture())));
    }

    @ParameterizedTest
    @MethodSource("getInvalidUsers")
    void createKo() throws Exception {
        // given
        Stream<User> invalidUsers = getInvalidUsers();


        //when
        mvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(invalidUsers))
                )

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll() {
    }

    @Test
    void findByIdNotFound() throws Exception {
        String id = "1";

        when(userService.findById(id)).thenReturn(Optional.empty());

        mvc.perform(get("/api/users/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById() throws Exception {
        // given
        User returnedUser = getUser();

        when(userService.findById(returnedUser.getId())).thenReturn(Optional.of(returnedUser));

        // when
        mvc.perform(get("/api/users/" + returnedUser.getId()))

        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedUser.getId())))
                .andExpect(jsonPath("$.email", is(returnedUser.getEmail())))
                .andExpect(jsonPath("$.password", is(returnedUser.getPassword())))
                .andExpect(jsonPath("$.name", is(returnedUser.getName())))
                .andExpect(jsonPath("$.picture", is(returnedUser.getPicture())));

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}