package service;

import domain.WebsiteRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WebsiteRegisterCache {

    @Autowired
    ConnectionUtils connectionUtils;
    @Value("${db.register.url}")
    String dbURL;

    private static Map<String, WebsiteRegister> websiteRegisterMap = new HashMap<>();

    public void initializeFromDB(){
        List<WebsiteRegister> websiteRegisters = Arrays.asList(connectionUtils.getAllRegisters(dbURL));
        log.info("Registers size;{};",websiteRegisters.size());
        websiteRegisters.forEach(item -> websiteRegisterMap.put(item.getName(), item));
    }

    public Optional<WebsiteRegister> read(String name) {
        return Optional.ofNullable(websiteRegisterMap.get(name.toLowerCase()));
    }

    public List<WebsiteRegister> read() {
        return websiteRegisterMap.values().stream().collect(Collectors.toList());
    }

    public void save(WebsiteRegister websiteCheck) {
        websiteRegisterMap.put(websiteCheck.getName().toLowerCase(), websiteCheck);
    }

    public void delete(String name) {
        websiteRegisterMap.remove(name);
    }

}