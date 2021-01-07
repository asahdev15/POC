package rest;

import domain.CustomException;
import domain.TypeInterval;
import domain.WebsiteRegister;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import service.UserService;
import service.WebsiteRegisterService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class RestWSWebsiteRegister {

    @Autowired
    WebsiteRegisterService websiteRegisterService;
    @Autowired
    UserService userService;

    @GetMapping(value = "/register")
    public ResponseEntity<List<WebsiteRegister>> get() {
        log.info("Reading All Register");
        List<WebsiteRegister> websiteRegister = websiteRegisterService.findAll();
        return new ResponseEntity<List<WebsiteRegister>>(websiteRegister, HttpStatus.OK);
    }

    @GetMapping(value = "/register/{name}")
    public ResponseEntity<WebsiteRegister> readWebsiteCheck(@PathVariable("name") String name) {
        log.info("Reading Register;{}",name);
        Optional<WebsiteRegister> websiteRegisterDB = websiteRegisterService.find(name);
        if (!websiteRegisterDB.isPresent()) {
            return returnCustomException("Website check not found;"+name, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<WebsiteRegister>(websiteRegisterDB.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/register/filter")
    public ResponseEntity<List<WebsiteRegister>> readFilters(
            @RequestParam(name="name", required=false) String name,
            @RequestParam(name="intervalType", required=false) String intervalType,
            @RequestParam(name="interval", required=false) String interval) {
        log.info("Reading Register;name;{};intervalType;{};interval;{}",name, intervalType, interval);
        Map<String, Object> params = new HashMap<>();
        if(Strings.isNotBlank(name)){
            params.put("name", name);
        }
        if(Strings.isNotBlank(intervalType)){
            Optional<ResponseEntity> inValidFreq = validateInputFrequencyType(intervalType);
            if(inValidFreq.isPresent()){
                return inValidFreq.get();
            }
            params.put("intervalType", intervalType.toString());
        }
        if(Strings.isNotBlank(interval)){
            try{
                int i = Integer.parseInt(interval);
                Optional<ResponseEntity> inValidFreq = validateInputFrequency(TypeInterval.valueOf(intervalType), i);
                if(inValidFreq.isPresent()){
                    return inValidFreq.get();
                }
                params.put("interval", interval);
            }catch(NumberFormatException ex){
                Optional<ResponseEntity> inValidFreq = Optional.of(returnCustomException("Invalid Interval time, it should be integer", HttpStatus.BAD_REQUEST));
                return inValidFreq.get();
            }
        }
        List<WebsiteRegister> websiteCheckDB = websiteRegisterService.findAll(params);
        return new ResponseEntity<List<WebsiteRegister>>(websiteCheckDB, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> register(@RequestBody WebsiteRegister websiteRegister, UriComponentsBuilder ucBuilder) {
        log.info("Saving Register");
        Optional<ResponseEntity> re = validateInput(websiteRegister);
        if(re.isPresent()){
            return re.get();
        }
        websiteRegister.setActive(true);
        websiteRegisterService.save(websiteRegister);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/register/{name}").buildAndExpand(websiteRegister.getName()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/register/enable/{name}")
    public ResponseEntity<Void> enable(@PathVariable("name") String name) {
        log.info("Enabling Register;{}",name);
        Optional<WebsiteRegister> websiteRegisterDB = websiteRegisterService.find(name);
        if (!websiteRegisterDB.isPresent()) {
            return returnCustomException("Website Register not found;"+name, HttpStatus.NOT_FOUND);
        }
        WebsiteRegister websiteRegister = websiteRegisterDB.get();
        websiteRegister.setActive(true);
        websiteRegisterService.save(websiteRegister);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/register/disable/{name}")
    public ResponseEntity<Void> disable(@PathVariable("name") String name) {
        log.info("Disabling Register;{}",name);
        Optional<WebsiteRegister> websiteRegisterDB = websiteRegisterService.find(name);
        if (!websiteRegisterDB.isPresent()) {
            return returnCustomException("Website Register not found;"+name, HttpStatus.NOT_FOUND);
        }
        WebsiteRegister websiteRegister = websiteRegisterDB.get();
        websiteRegister.setActive(false);
        websiteRegisterService.save(websiteRegister);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    private Optional<ResponseEntity> validateInput(WebsiteRegister websiteRegister){
        if(!userService.find(websiteRegister.getUsername()).isPresent()){
            return Optional.of(returnCustomException("User not found;name;"+websiteRegister.getUsername(), HttpStatus.NOT_FOUND));
        }
        Optional<ResponseEntity> inValidFreq = validateInputFrequency(websiteRegister.getIntervalType(), websiteRegister.getInterval());
        if(inValidFreq.isPresent()){
            return inValidFreq;
        }
        if(!isURLValid(websiteRegister.getUrl())){
            return Optional.of(returnCustomException("Invalid URL;"+websiteRegister.getUrl(), HttpStatus.BAD_REQUEST));
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity> validateInputFrequencyType(String intervalType){
        if(!TypeInterval.MINS.name().equals(intervalType) && !TypeInterval.HOURS.name().equals(intervalType)){
            return Optional.of(returnCustomException("Invalid Frequency Type; it should be between MINS or HOURS;", HttpStatus.BAD_REQUEST));
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity> validateInputFrequency(TypeInterval intervalType, int interval){
        if(TypeInterval.MINS.equals(intervalType) && (interval<1 || interval>59)){
            return Optional.of(returnCustomException("Invalid Interval time for mins; it should be between 1-59 mins;", HttpStatus.BAD_REQUEST));
        }
        if(TypeInterval.HOURS.equals(intervalType) && (interval<1 || interval>24)){
            return Optional.of(returnCustomException("Invalid Interval time for hours; it should be between 1-24 hours;", HttpStatus.BAD_REQUEST));
        }
        return Optional.empty();
    }

    private boolean isURLValid(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException exception) {
            return false;
        }
        return true;
    }

    private ResponseEntity returnCustomException(String message, HttpStatus httpStatus){
        return new ResponseEntity(new CustomException(message), httpStatus);
    }

}
