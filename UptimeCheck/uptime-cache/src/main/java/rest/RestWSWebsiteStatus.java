package rest;

import domain.WebsiteStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.WebsiteStatusCache;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class RestWSWebsiteStatus {

    @Autowired
    WebsiteStatusCache websiteStatusCache;

    @GetMapping(value = "/status")
    public ResponseEntity<List<WebsiteStatus>> get() {
        log.info("Reading All website status");
        List<WebsiteStatus> websiteStatus = websiteStatusCache.findAll();
        return new ResponseEntity<List<WebsiteStatus>>(websiteStatus, HttpStatus.OK);
    }

    @GetMapping(value = "/status/names")
    public ResponseEntity<List<WebsiteStatus>> get(@RequestBody List<String> names) {
        log.info("Reading All website status;names size{}", names.size());
        List<WebsiteStatus> websiteStatus = websiteStatusCache.findByNames(names);
        return new ResponseEntity<List<WebsiteStatus>>(websiteStatus, HttpStatus.OK);
    }

    @GetMapping(value = "/status/{name}")
    public ResponseEntity<WebsiteStatus> get(@PathVariable("name") String name) {
        log.info("Reading website status;{}",name);
        Optional<WebsiteStatus> websiteStatus = websiteStatusCache.findByName(name);
        if (!websiteStatus.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<WebsiteStatus>(websiteStatus.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/status")
    public ResponseEntity<Void> save(@RequestBody WebsiteStatus websiteStatus) {
        log.info("Saving website status");
        websiteStatusCache.save(websiteStatus);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/status/{name}")
    public ResponseEntity<Void> delete(@PathVariable("name") String name) {
        log.info("Deleting website status;{}",name);
        websiteStatusCache.delete(name);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
