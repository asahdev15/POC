package rest;

import domain.CustomException;
import domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class RestWSUser {

    @Autowired
    UserService userService;

    @GetMapping(value = "/user")
    public ResponseEntity<List<User>> get() {
        log.info("Reading All users");
        List<User> users = userService.findAll();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{name}")
    public ResponseEntity<User> get(@PathVariable("name") String name) {
        log.info("Reading user by name;{}", name);
        Optional<User> user = userService.find(name);
        if (!user.isPresent()) {
            return new ResponseEntity(new CustomException("User not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/user")
    public ResponseEntity<Void> save(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        log.info("Saving user");
        userService.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{name}").buildAndExpand(user.getName()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

}