package com.enigma.resttemplate.controller;

import com.enigma.resttemplate.entities.Posts;
import com.enigma.resttemplate.response.PostResponse;
import com.enigma.resttemplate.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Posts[]> getAllPosts() {
      return postService.getAllPosts();
    }

    @GetMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPostById(@PathVariable String id) {
       return postService.getPostById(id);
    }

    @GetMapping(path = "/comments{postId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPostCommentsByPostId(@RequestParam Long postId) {
      return postService.getPostCommentsByPostId(postId);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPost(@RequestBody Posts request){
        return postService.createPost(request);
    }

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Posts> createPost(@RequestBody Posts request){
//        String apiUrls = "https://jsonplaceholder.typicode.com/posts";
//        Posts posts = restTemplate.postForObject(apiUrls, request, Posts.class);
//        return ResponseEntity.ok(posts);
//    }

    @GetMapping(value = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPostByIdComments(@PathVariable Integer postId){
        return postService.getPostsCommentById(postId);
    }

}
