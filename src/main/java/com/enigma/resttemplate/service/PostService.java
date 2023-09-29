package com.enigma.resttemplate.service;

import com.enigma.resttemplate.entities.Posts;
import com.enigma.resttemplate.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PostService {
    private final RestTemplate restTemplate;
    // kode lebih rapih (clean code)
    // tidak ada data yang redundan (refactoring)

    @Value("${api.endpoint.url.post}")
    private String BASE_URL;

    public ResponseEntity<Posts[]> getAllPosts() {
        String apiUrls = BASE_URL + "/posts";

        return getResponseEntity(apiUrls);
    }

    private ResponseEntity<String> responseMethodGet(ResponseEntity<String> restTemplate) {
        ResponseEntity<String> responseEntity = restTemplate;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            return ResponseEntity.ok(responseBody);
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body("Failed API");
        }
    }
    private ResponseEntity<Posts[]> getResponseEntity(String apiUrl) {
        ResponseEntity<Posts[]> responseEntity = restTemplate.getForEntity(apiUrl, Posts[].class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Posts[] responseBody = responseEntity.getBody();
            return ResponseEntity.ok(responseBody);
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
        }
    }

    public ResponseEntity<String> getPostById(String id) {
        String apiUrls = BASE_URL + "/posts/"+id;
        return responseMethodGet(restTemplate.getForEntity(apiUrls, String.class));
    }

    public ResponseEntity<String> getPostCommentsByPostId(Long postId) {
        String apiUrls = BASE_URL + "/comments?postId="+postId;
        return responseMethodGet(restTemplate.getForEntity(apiUrls, String.class));
    }

    public ResponseEntity<String> createPost(Posts request){
        String apiUrls = BASE_URL + "/posts";
        // set request header
        HttpHeaders headers = new HttpHeaders();
        // bungkus data request dalam http entity
        HttpEntity<Posts> requestEntity = new HttpEntity<>(request, headers);
        return responseMethodGet(restTemplate.postForEntity(apiUrls, requestEntity, String.class));
    }

    public ResponseEntity<String> getPostsCommentById(Integer postId) {
        String apiUrl = BASE_URL + "/posts/" + postId + "/comments";
        return responseMethodGet(restTemplate.getForEntity(apiUrl, String.class));
    }
}
