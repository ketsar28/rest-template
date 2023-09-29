package com.enigma.resttemplate.controller;

import com.enigma.resttemplate.entities.Post;
import com.enigma.resttemplate.response.PostResponse;
import com.enigma.resttemplate.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostResponse>> getAllPosts() {
      return postService.getAllPosts();
    }

    @GetMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> getPostById(@PathVariable String id) {
       return postService.getPostById(id);
    }

    @GetMapping(path = "/comments{postId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> getPostCommentsByPostId(@RequestParam Long postId) {
      return postService.getPostCommentsByPostId(postId);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> createPost(@RequestBody Post request){
        return postService.createPost(request);
    }

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Posts> createPost(@RequestBody Posts request){
//        String apiUrls = "https://jsonplaceholder.typicode.com/posts";
//        Posts posts = restTemplate.postForObject(apiUrls, request, Posts.class);
//        return ResponseEntity.ok(posts);
//    }

}
