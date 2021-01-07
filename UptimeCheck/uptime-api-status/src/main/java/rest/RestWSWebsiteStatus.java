package rest;

import domain.CustomException;
import domain.TypeInterval;
import domain.WebsiteRegister;
import domain.WebsiteStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import service.WebsiteStatusService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class RestWSWebsiteStatus {

    @Autowired
    WebsiteStatusService websiteStatusService;

    @GetMapping(value = "/status")
    public ResponseEntity<List<WebsiteStatus>> get() {
        log.info("Reading All website register");
        List<WebsiteStatus> websiteStatus = websiteStatusService.findAll();
        return new ResponseEntity<List<WebsiteStatus>>(websiteStatus, HttpStatus.OK);
    }

    @GetMapping(value = "/status/{name}")
    public ResponseEntity<WebsiteStatus> read(@PathVariable("name") String name) {
        log.info("Reading Status;{}",name);
        Optional<WebsiteStatus> ws = websiteStatusService.findInCache(name);
        if (!ws.isPresent()) {
            ws = websiteStatusService.findInDB(name);
            if (!ws.isPresent()) {
                return returnCustomException("Website Status not found;"+name, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<WebsiteStatus>(ws.get(), HttpStatus.OK);
    }

    private ResponseEntity returnCustomException(String message, HttpStatus httpStatus){
        return new ResponseEntity(new CustomException(message), httpStatus);
    }

}
