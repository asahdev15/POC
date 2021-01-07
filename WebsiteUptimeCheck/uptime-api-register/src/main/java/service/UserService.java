package service;

import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClientException;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    ConnectionUtils connectionUtils;
    @Value("${db.user.url}")
    String dbURL;

    public List<User> findAll() {
        return connectionUtils.getAll(dbURL, List.class, new HashMap<>());
    }

    public Optional<User> find(String name) {
        return connectionUtils.get(dbURL+"/"+name, User.class);
    }

    public void save(User user) {
        connectionUtils.post(dbURL, User.class, user);
    }

}