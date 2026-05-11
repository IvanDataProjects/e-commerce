package com.gestor.e_commerce.client;

import com.gestor.e_commerce.dto.UserDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

@Component
public class UserClient {

    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;

    public UserClient(RestTemplate restTemplate, TokenProvider tokenProvider) {
        this.restTemplate = restTemplate;
        this.tokenProvider = tokenProvider;
    }

    public UserDTO getUser(Long id) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenProvider.getToken());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> userResponse = restTemplate.exchange("http://user-service:8081/e-commerce/user/" + id, HttpMethod.GET, requestEntity, UserDTO.class);

        return userResponse.getBody();
    }

    // GET ALL USERS
    public List<UserDTO> getAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenProvider.getToken());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO[]> response = restTemplate.exchange("http://user-service:8081/e-commerce/user", HttpMethod.GET, requestEntity, UserDTO[].class);

        assert response.getBody() != null;
        return Arrays.asList(response.getBody());
    }
}