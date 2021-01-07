package rest;

import domain.CustomException;
import domain.User;
import domain.WebsiteRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import service.UserDB;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class RestWSUser {

    @Autowired
    UserDB userService;

    @GetMapping(value = "/user")
    public ResponseEntity<List<User>> get() {
        log.info("Reading All users");
        List<User> users = userService.findAll();
        log.info("Found;size;{}",users.size());
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{name}")
    public ResponseEntity<User> readUser(@PathVariable("name") String name) {
        log.info("Reading user;name;{}", name);
        Optional<User> user = userService.find(name);
        if (!user.isPresent()) {
            log.info("DB;User not found;{}", name);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/user")
    public ResponseEntity<Void> saveUser(@RequestBody User user) {
        log.info("Saving user");
        userService.save(user);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}