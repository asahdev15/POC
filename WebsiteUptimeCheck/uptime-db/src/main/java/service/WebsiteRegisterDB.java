package service;

import domain.TypeInterval;
import domain.WebsiteRegister;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WebsiteRegisterDB {

    private static Map<String, WebsiteRegister> websiteCheckMap = new HashMap<>();

    public Optional<WebsiteRegister> find(String name) {
        return Optional.ofNullable(websiteCheckMap.get(name.toLowerCase()));
    }

    public List<WebsiteRegister> find(String name, String typeInterval, Integer interval) {
        return
                websiteCheckMap.values().stream()
                .filter(wr -> name==null || wr.getName().contains(name.toLowerCase()))
                .filter(wr -> typeInterval==null || wr.getIntervalType().equals(TypeInterval.valueOf(typeInterval)))
                .filter(wr -> interval==null || wr.getInterval() == interval)
                .collect(Collectors.toList());
    }

    public List<WebsiteRegister> findAll() {
        return websiteCheckMap.values().stream().collect(Collectors.toList());
    }

    public void save(WebsiteRegister websiteCheck) {
        websiteCheckMap.put(websiteCheck.getName().toLowerCase(), websiteCheck);
    }

}