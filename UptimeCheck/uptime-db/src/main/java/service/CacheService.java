package service;

import domain.WebsiteRegister;
import domain.WebsiteStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
public class CacheService {

    @Autowired
    ConnectionUtils connectionUtils;
    @Value("${cache.register.url}")
    String cacheRegisterURL;
    @Value("${cache.status.url}")
    String cacheStatusURL;

    public void pushToCache(WebsiteRegister websiteCheck){
        log.info("Updating cache with Register");
        connectionUtils.post(cacheRegisterURL, WebsiteRegister.class, websiteCheck);
    }

    public void pushToCache(WebsiteStatus websiteStatus){
        log.info("Updating cache with Status");
        connectionUtils.post(cacheStatusURL, WebsiteStatus.class, websiteStatus);
    }

}
