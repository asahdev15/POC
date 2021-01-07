package rest;

import domain.WebsiteRegister;
import domain.WebsiteStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.WebsiteRegisterCache;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class RestWSWebsiteRegister {

    @Autowired
    WebsiteRegisterCache websiteRegisterCache;

    @GetMapping(value = "/register")
    public ResponseEntity<List<WebsiteRegister>> get() {
        log.info("Reading All website register");
        List<WebsiteRegister> websiteRegister = websiteRegisterCache.read();
        return new ResponseEntity<List<WebsiteRegister>>(websiteRegister, HttpStatus.OK);
    }

    @GetMapping(value = "/register/{name}")
    public ResponseEntity<WebsiteRegister> get(@PathVariable("name") String name) {
        log.info("Fetching Register;{}",name);
        Optional<WebsiteRegister> websiteCheckDB = websiteRegisterCache.read(name);
        if (!websiteCheckDB.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<WebsiteRegister>(websiteCheckDB.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> save(@RequestBody WebsiteRegister websiteCheck) {
        log.info("Saving Register");
        websiteRegisterCache.save(websiteCheck);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/register/{name}")
    public ResponseEntity<Void> delete(@PathVariable("name") String name) {
        log.info("Deleting check;{}",name);
        websiteRegisterCache.delete(name);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
