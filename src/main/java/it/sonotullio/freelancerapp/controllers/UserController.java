package it.sonotullio.freelancerapp.controllers;

import it.sonotullio.freelancerapp.exceptions.NotFoundException;
import it.sonotullio.freelancerapp.model.User;
import it.sonotullio.freelancerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable String id) {
        return userRepository.findById(id).orElseThrow( () -> new NotFoundException("User not found") );
    }

    @PutMapping("/{id}")
    public User update(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(String id) {
        userRepository.deleteByEmail(id);
    }
}
