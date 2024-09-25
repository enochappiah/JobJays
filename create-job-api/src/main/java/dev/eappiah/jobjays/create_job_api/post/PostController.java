package dev.eappiah.jobjays.create_job_api.post;


import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final PostRepository postRepository;

  public PostController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @GetMapping("")
  public List<Post> findAll() {
    return postRepository.findAll();
  }

  @GetMapping("/{id}")
  Post findById(@PathVariable Integer id) {
    Optional<Post> post = postRepository.findById(id);
    if (post.isEmpty()) {
      throw new PostNotFoundException();
    }
    return post.get();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("")
  void addPost(@RequestBody Post post) {
    postRepository.addPost(post);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/{id}")
  void updatePost(@RequestBody Post post, @PathVariable Integer id) {
    postRepository.update(post, id);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  void deletePost(@PathVariable Integer id) {
    postRepository.delete(id);
  }

}
