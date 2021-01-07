package rest;

import domain.CustomException;
import domain.TypeInterval;
import domain.WebsiteRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CacheService;
import service.WebsiteRegisterDB;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class RestWSWebsiteRegister {

    @Autowired
    WebsiteRegisterDB websiteCheckService;
    @Autowired
    CacheService cacheService;

    @GetMapping(value = "/register")
    public ResponseEntity<List<WebsiteRegister>> get() {
        log.info("Reading All register");
        List<WebsiteRegister> websiteRegister = websiteCheckService.findAll();
        log.info("Found;size;{}",websiteRegister.size());
        return new ResponseEntity<List<WebsiteRegister>>(websiteRegister, HttpStatus.OK);
    }

    @GetMapping(value = "/register/{name}")
    public ResponseEntity<WebsiteRegister> read(@PathVariable("name") String name) {
        log.info("Reading Register;name;{}",name);
        Optional<WebsiteRegister> websiteCheckDB = websiteCheckService.find(name);
        if(!websiteCheckDB.isPresent()){
            return new ResponseEntity<WebsiteRegister>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<WebsiteRegister>(websiteCheckDB.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/register/filter")
    public ResponseEntity<List<WebsiteRegister>> readFilters(
            @RequestParam(name="name", required=false) String name,
            @RequestParam(name="intervalType", required=false) String intervalType,
            @RequestParam(name="interval", required=false) String interval) {
        log.info("Reading Register;name;{};intervalType;{};interval;{}",name, intervalType, interval);
        List<WebsiteRegister> websiteRegister = websiteCheckService.find(name, intervalType, interval!=null?Integer.parseInt(interval):null);
        log.info("Found;size;{}",websiteRegister.size());
        return new ResponseEntity<List<WebsiteRegister>>(websiteRegister, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> save(@RequestBody WebsiteRegister websiteCheck) {
        log.info("Saving Register");
        websiteCheckService.save(websiteCheck);
        cacheService.pushToCache(websiteCheck);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity returnCustomException(String message, HttpStatus httpStatus){
        return new ResponseEntity(new CustomException(message), httpStatus);
    }

}
