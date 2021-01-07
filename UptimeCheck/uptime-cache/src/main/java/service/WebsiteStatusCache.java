package service;

import domain.WebsiteRegister;
import domain.WebsiteStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WebsiteStatusCache {

    @Autowired
    ConnectionUtils connectionUtils;
    @Value("${db.status.url}")
    String dbURL;

    private static Map<String, WebsiteStatus> websiteCheckStatusMap = new HashMap<>();

    public void initializeFromDB(){
        List<WebsiteStatus> websiteStatuses = Arrays.asList(connectionUtils.getAllStatuses(dbURL));
        websiteStatuses.forEach(item -> websiteCheckStatusMap.put(item.getName(), item));
        log.info("Statuses size;{};",websiteStatuses.size());
    }

    public List<WebsiteStatus> findAll() {
        return websiteCheckStatusMap.values().stream().collect(Collectors.toList());
    }

    public Optional<WebsiteStatus> findByName(String name) {
        return Optional.ofNullable(websiteCheckStatusMap.get(name.toLowerCase()));
    }

    public List<WebsiteStatus> findByNames(List<String> names) {
        return websiteCheckStatusMap.values().stream().filter(item -> names.contains(item)).collect(Collectors.toList());
    }

    public void save(WebsiteStatus websiteCheck) {
        websiteCheckStatusMap.put(websiteCheck.getName().toLowerCase(), websiteCheck);
    }

    public void delete(String name) {
        websiteCheckStatusMap.remove(name);
    }

}