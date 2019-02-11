package com.lemi.njp_projekat.rest_controllers;

import com.lemi.njp_projekat.entities.Post;
import com.lemi.njp_projekat.entities.User;
import com.lemi.njp_projekat.repository.PostRepository;
import com.lemi.njp_projekat.repository.UserRepository;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository repository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get")
    List<Post> getPostsFromFollowed(@RequestParam String username) {
        List<Post> res = new ArrayList<>();
        User user = userRepository.findByUsername(username);
        res.addAll(user.getPosts());
        for (User u : user.getFollowing()){
            res.addAll(u.getPosts());
        }

        return res;
    }

    @GetMapping
    List<Post> getAllPosts(){ return repository.findAll();}

    @PostMapping("/add")
    public String addPost(@RequestBody Post post, @RequestParam String username){
        User user = userRepository.findByUsername(username);
        user.addPost(post);
        repository.save(post);
        userRepository.save(user);
        return "Dodao post korisniku " + post.getUser().getUsername() + "!";
    }

}
