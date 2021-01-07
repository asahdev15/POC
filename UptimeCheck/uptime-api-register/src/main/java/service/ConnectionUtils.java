package service;

import domain.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<T> requestEntity = new HttpEntity<>(requestHeaders);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
            params.entrySet().stream().forEach(item -> uriBuilder.queryParam(item.getKey(), item.getValue()));
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    objectClass
            );
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