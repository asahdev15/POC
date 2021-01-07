package service;

import domain.WebsiteStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WebsiteStatusDB {

    private static Map<String, WebsiteStatus> websiteCheckStatusMap = new HashMap<>();

    public Optional<WebsiteStatus> find(String name) {
        return Optional.ofNullable(websiteCheckStatusMap.get(name.toLowerCase()));
    }

    public List<WebsiteStatus> findAll() {
        return websiteCheckStatusMap.values().stream().collect(Collectors.toList());
    }

    public void register(WebsiteStatus websiteCheck) {
        websiteCheckStatusMap.put(websiteCheck.getName().toLowerCase(), websiteCheck);
    }

}