package rest;

import domain.CustomException;
import domain.WebsiteStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CacheService;
import service.WebsiteStatusDB;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class RestWSWebsiteStatus {

    @Autowired
    WebsiteStatusDB websiteStatusDB;

    @GetMapping(value = "/status")
    public ResponseEntity<List<WebsiteStatus>> get() {
        log.info("Reading All status");
        List<WebsiteStatus> websiteStatus = websiteStatusDB.findAll();
        log.info("Found;size;{}",websiteStatus.size());
        return new ResponseEntity<List<WebsiteStatus>>(websiteStatus, HttpStatus.OK);
    }

    @GetMapping(value = "/status/{name}")
    public ResponseEntity<WebsiteStatus> get(@PathVariable("name") String name) {
        log.info("Reading status;{}",name);
        Optional<WebsiteStatus> websiteStatus = websiteStatusDB.find(name);
        return new ResponseEntity<WebsiteStatus>(websiteStatus.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/status")
    public ResponseEntity<Void> updateStatus(@RequestBody WebsiteStatus websiteStatus) {
        log.info("Updating status");
        websiteStatusDB.register(websiteStatus);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
