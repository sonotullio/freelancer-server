package it.sonotullio.freelancerapp.controllers;

import it.sonotullio.freelancerapp.model.User;
import it.sonotullio.freelancerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    public User register(@RequestBody User user) throws Exception {
        Optional<User> alreadyExist = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (alreadyExist.isPresent()) {
            throw new Exception("User already exist");
        } else {
            return userRepository.save(user);
        }
    }

}
