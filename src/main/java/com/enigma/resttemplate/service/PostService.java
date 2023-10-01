package com.enigma.resttemplate.service;

import com.enigma.resttemplate.entities.Post;
import com.enigma.resttemplate.repository.PostRepository;
import com.enigma.resttemplate.response.PostResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final RestTemplate restTemplate;
    private final PostRepository postRepository;
    private final ObjectMapper objectMapper;
    // kode lebih rapih (clean code)
    // tidak ada data yang redundan (refactoring)

    @Value("${api.endpoint.url.post}")
    private String BASE_URL;

    private static PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
                .idPost(post.getId())
                .userIdPost(post.getUserId())
                .titlePost(post.getTitle())
                .bodyPost(post.getBody())
                .build();
    }

    private ResponseEntity<PostResponse> responseMethodGet(ResponseEntity<Post> restTemplate) {
        if (restTemplate.getStatusCode().is2xxSuccessful()) {
            Post responseBody = restTemplate.getBody();
            PostResponse postResponse = PostResponse.builder()
                    .idPost(Objects.requireNonNull(responseBody).getId())
                    .userIdPost(responseBody.getUserId())
                    .titlePost(responseBody.getTitle())
                    .bodyPost(responseBody.getBody())
                    .build();

            return ResponseEntity.ok(postResponse);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "failed api");
        }
    }

    private ResponseEntity<List<PostResponse>> getResponseEntity(String apiUrl) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
          try{
              String responseBody = responseEntity.getBody();
              List<Post> posts = objectMapper.readValue(responseBody, new TypeReference<>(){});

              List<PostResponse> postResponses = posts.stream()
                      .map(PostService::toPostResponse)
                      .collect(Collectors.toList());

              return ResponseEntity.ok(postResponses);
          }catch(IOException exception) {
              exception.printStackTrace();
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
          }
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
        }
    }

    public ResponseEntity<List<PostResponse>> getAllPosts() {
        String apiUrls = BASE_URL + "/posts";
        return getResponseEntity(apiUrls);
    }

    public ResponseEntity<PostResponse> getPostById(Integer id) {
        String apiUrls = BASE_URL + "/posts/"+id;
        return responseMethodGet(restTemplate.getForEntity(apiUrls, Post.class));
    }

    public ResponseEntity<PostResponse> getPostCommentsByPostId(Integer postId) {
        String apiUrls = BASE_URL + "/comments?postId="+postId;
        return responseMethodGet(restTemplate.getForEntity(apiUrls, Post.class));
    }

    public ResponseEntity<PostResponse> createPost(Post request){
        try {
            String apiUrls = BASE_URL + "/posts";
            // set request header
            HttpHeaders headers = new HttpHeaders();
            // bungkus data request dalam http entity
            HttpEntity<Post> requestEntity = new HttpEntity<>(request, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrls, HttpMethod.POST, requestEntity, String.class);
            Post post = objectMapper.readValue(responseEntity.getBody(), Post.class);
            postRepository.save(post);

            PostResponse response = toPostResponse(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<PostResponse> updatePost(Post request, Integer id){
       Optional<Post> isExistsPost = postRepository.findById(id);

        if(isExistsPost.isPresent()) {
            Post post = isExistsPost.get();

            post.setTitle(request.getTitle());
            post.setBody(request.getBody());
            post = postRepository.save(post);

            PostResponse postResponse = toPostResponse(post);

            return ResponseEntity.ok(postResponse);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found");
    }

    public ResponseEntity<String> deletePost(Integer id) {
        Optional<Post> isPostExists = postRepository.findById(id);
        if(isPostExists.isPresent()) {
            postRepository.deleteById(id);
            return ResponseEntity.ok("data in " + id + " has been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found");
        }
    }
}
