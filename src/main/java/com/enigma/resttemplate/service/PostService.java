package com.enigma.resttemplate.service;

import com.enigma.resttemplate.dto.request.PostRequest;
import com.enigma.resttemplate.dto.response.PostCommentResponse;
import com.enigma.resttemplate.entities.Post;
import com.enigma.resttemplate.entities.PostComment;
import com.enigma.resttemplate.repository.PostCommentRepository;
import com.enigma.resttemplate.repository.PostRepository;
import com.enigma.resttemplate.dto.response.PostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final RestTemplate restTemplate;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
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
    private ResponseEntity<List<PostCommentResponse>> responseMethodGetPostComment(String apiUrl) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            List<PostComment> postComments = objectMapper.readValue(responseBody, new TypeReference<>() {});
            List<PostCommentResponse> postCommentResponses = postComments.stream()
                    .map(postComment -> PostCommentResponse.builder()
                            .idPost(postComment.getPostId())
                            .userIdPost(postComment.getId())
                            .namePost(postComment.getName())
                            .emailPost(postComment.getEmail())
                            .bodyPost(postComment.getBody())
                            .build())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(postCommentResponses);
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

    public ResponseEntity<List<PostCommentResponse>> getPostCommentsByPostId(Integer postId) throws JsonProcessingException {
        String apiUrls = BASE_URL + "/comments?postId="+postId;
        return responseMethodGetPostComment(apiUrls);
    }

    public ResponseEntity<PostResponse> createPost(PostRequest request){
        try {
            String apiUrls = BASE_URL + "/posts";
            // set request header
            HttpHeaders headers = new HttpHeaders();
            // bungkus data request dalam http entity
            HttpEntity<PostRequest> requestEntity = new HttpEntity<>(request, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrls, HttpMethod.POST, requestEntity, String.class);
            PostRequest post = objectMapper.readValue(responseEntity.getBody(), PostRequest.class);
            Post savePost = Post.builder()
                    .id(post.getIdPost())
                    .title(post.getTitlePost())
                    .body(post.getBodyPost())
                    .userId(post.getUserIdPost())
                    .build();

            postRepository.save(savePost);

            PostResponse response = toPostResponse(savePost);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
