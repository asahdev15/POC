package service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ConnectionUtils {

    public <T> Optional<T> get(String url, Class objectClass){
        try{
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity responseEntity = restTemplate.getForEntity(url, objectClass);
            if(HttpStatus.OK.equals(responseEntity.getStatusCode())){
                return Optional.of((T)responseEntity.getBody());
            }
        }catch (RestClientException ex){
            System.out.println(ex);
        }
        return Optional.empty();
    }

    public <T> List<T> getAll(String url, Class objectClass, Map<String, Object> params){
        try{
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity responseEntity = restTemplate.getForEntity(url, objectClass, params);
            if(HttpStatus.OK.equals(responseEntity.getStatusCode())){
                return (List<T>) responseEntity.getBody();
            }
        }catch (RestClientException ex){
            System.out.println(ex);
        }
        return new ArrayList<>();
    }

    public ResponseEntity<Void> post(String url, Class objectClass, Object item) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url, item, objectClass, new HashMap<>());
    }

    public void update(String url, Class objectClass, Object item) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, item);
    }

    public void delete(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
    }

}