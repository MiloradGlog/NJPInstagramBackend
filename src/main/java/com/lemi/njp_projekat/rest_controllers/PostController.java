package com.lemi.njp_projekat.rest_controllers;

import com.lemi.njp_projekat.entities.Post;
import com.lemi.njp_projekat.entities.User;
import com.lemi.njp_projekat.repository.PostRepository;
import com.lemi.njp_projekat.repository.UserRepository;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository repository;

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOADED_FOLDER = "E:\\Projekti\\Angular\\InstagramNJP\\src\\assets\\users\\";

    @GetMapping("/getStories")
    List<String> getStories(@RequestParam String username){
        List<String> list = new ArrayList<>();
        User user = userRepository.findByUsername(username);
        for (Post p : user.getPosts()){
            list.add(p.getImageURL().replace("\\", "/"));
        }
        return list;
    }

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
/*
    @PostMapping("/add")
    public String addPost(@RequestBody Post post, @RequestParam String username){
        User user = userRepository.findByUsername(username);
        user.addPost(post);
        repository.save(post);
        userRepository.save(user);
        return "Dodao post korisniku " + post.getUser().getUsername() + "!";
    }
*/


    @PostMapping(value = "/upload")
    public Post uploadPost(@RequestBody Post post, @RequestParam String username) {
        String[] base64 = post.getImageURL().split(",");
        String[] formats = base64[0].split("/");
        formats = formats[1].split(";");
        String format = formats[0];
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String location = UPLOADED_FOLDER + username + "\\" + timestamp.getTime() + "." + format;
        try {
            byte[] imgByte = Base64.getDecoder().decode(base64[1]);
            Path path = Paths.get(location);
            Files.write(path, imgByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        post.setImageURL("assets\\users\\" + username + "\\" + timestamp.getTime() + "." + format);
        User user = userRepository.findByUsername(username);
        user.addPost(post);
        repository.save(post);
        userRepository.save(user);
        return post;
    }
}
