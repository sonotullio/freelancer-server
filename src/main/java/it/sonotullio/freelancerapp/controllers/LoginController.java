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
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    public User login(@RequestBody User user) throws Exception {
        Optional<User> alreadyExist = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (alreadyExist.isPresent()) {
            return alreadyExist.get();
        } else {
            throw new Exception("User not found");
        }
    }

    @PostMapping("/google")
    public User loginGoogle(@RequestBody User user) {
        // uso questa scorciatoia per il login da google, se l'utente non esiste lo creo
        Optional<User> alreadyExist = userRepository.findByEmail(user.getEmail());
        if (alreadyExist.isPresent()) {
            return alreadyExist.get();
        }
        return userRepository.save(user);
    }
}
