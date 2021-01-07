package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.WebsiteRegister;
import domain.WebsiteStatus;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class ConnectionUtils {

    public WebsiteRegister[] getAllRegisters(String url){
        try{
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(url, WebsiteRegister[].class).getBody();
        }catch (RestClientException ex){
            System.out.println("Error in calling REST : " + ex);
        }
        return new WebsiteRegister[0];
    }

    public WebsiteStatus[] getAllStatuses(String url){
        try{
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(url, WebsiteStatus[].class).getBody();
        }catch (RestClientException ex){
            System.out.println("Error in calling REST : " + ex);
        }
        return new WebsiteStatus[0];
    }

}