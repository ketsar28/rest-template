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
    public ResponseEntity<PostResponse> getPostById(@PathVariable Integer id) {
       return postService.getPostById(id);
    }

    @GetMapping(path = "/comments{postId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostResponse>> getPostCommentsByPostId(@RequestParam Integer postId) {
      return postService.getPostCommentsByPostId(postId);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> createPost(@RequestBody Post request){
        return postService.createPost(request);
    }

    @PutMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> updatePost(@RequestBody Post request, @PathVariable Integer id){
        return postService.updatePost(request, id);
    }

    @DeleteMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deletePost(@PathVariable Integer id){
        return postService.deletePost(id);
    }
}
