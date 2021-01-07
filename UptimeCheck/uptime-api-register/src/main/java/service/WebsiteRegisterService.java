package service;

import domain.User;
import domain.WebsiteRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WebsiteRegisterService {

    @Autowired
    ConnectionUtils connectionUtils;
    @Value("${db.register.url}")
    String dbURL;

    public List<WebsiteRegister> findAll() {
        return connectionUtils.getAll(dbURL, List.class, new HashMap<>());
    }

    public List<WebsiteRegister> findAll(Map<String, Object> params) {
        return connectionUtils.getAll(dbURL+"/filter", List.class, params);
    }

    public Optional<WebsiteRegister> find(String name) {
        return connectionUtils.get(dbURL+"/"+name, WebsiteRegister.class);
    }

    public void save(WebsiteRegister websiteRegister) {
        connectionUtils.post(dbURL, WebsiteRegister.class, websiteRegister);
    }

}