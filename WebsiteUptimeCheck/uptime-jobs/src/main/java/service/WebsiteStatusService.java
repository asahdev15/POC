package service;

import domain.WebsiteRegister;
import domain.WebsiteStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class WebsiteStatusService {

    @Autowired
    ConnectionUtils connectionUtils;
    @Value("${cache.status.url}")
    String cacheURL;
    @Value("${db.status.url}")
    String dbURL;

    public List<WebsiteStatus> fetchAll() {
        return connectionUtils.getAllStatuses(dbURL);
    }

    public Optional<WebsiteStatus> findInCache(String name) {
        return connectionUtils.get(cacheURL+"/"+name, WebsiteStatus.class);
    }

    public void updateCache(WebsiteStatus websiteStatus) {
        connectionUtils.post(cacheURL, WebsiteStatus.class, websiteStatus);
    }

    public void updateDB(WebsiteStatus websiteStatus) {
        connectionUtils.post(dbURL, WebsiteStatus.class, websiteStatus);
    }

}