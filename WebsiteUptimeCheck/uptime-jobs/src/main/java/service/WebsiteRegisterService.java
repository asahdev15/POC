package service;

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
    @Value("${cache.register.url}")
    String url;

    public List<WebsiteRegister> fetchAll() {
        return connectionUtils.getAllRegisters(url);
    }

}