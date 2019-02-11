package com.lemi.njp_projekat.rest_controllers;

import com.lemi.njp_projekat.entities.User;
import com.lemi.njp_projekat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @GetMapping("/getByUsername")
    public User getUserByUsername(@RequestParam String username) { return userRepository.findByUsername(username); }

    @GetMapping("/followers")
    public List<User> getFollowers(@RequestParam String username){
        return userRepository.findByUsername(username).getFollowers();
    }

    @GetMapping("/following")
    public List<User> getFollowing(@RequestParam String username){
        return userRepository.findByUsername(username).getFollowing();
    }

    @PostMapping("/register")
    public String register(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepository.findByUsername(user.getUsername()) != null)
            return "USER ALREADY EXISTS";
        userRepository.save(user);
        return "Registracija uspesna";
    }

    @GetMapping("/follow")
    public String follow(@RequestParam String srcUsername, @RequestParam String trgtUsername){
        User usr = userRepository.findByUsername(srcUsername);
        User trgt = userRepository.findByUsername(trgtUsername);
        if (usr == null || trgt == null) return "ERROR, USER DOESNT EXIST";

        usr.followOtherUser(trgt);
        userRepository.save(usr);
        userRepository.save(trgt);
        return srcUsername +" SUCESSFULLY FOLLOWED "+ trgtUsername;
    }

    @GetMapping("/isFollowing")
    public boolean isFollowing(@RequestParam String srcUsername, @RequestParam String trgtUsername){
        User srcUser = userRepository.findByUsername(srcUsername);
        User trgtUser = userRepository.findByUsername(trgtUsername);
        if (srcUser.getFollowing().contains(trgtUser)) return true;
        else return false;
    }

 /*   @GetMapping("/setCurrentUser")
    public String setCurrentUser(@RequestParam String username){
        User user = userRepository.findByUsername(username);
        stateManager.setCurrentUser(user);
        System.out.println("stateManager user u UserController: "+ stateManager.getCurrentUser().getUsername());
        return "Uspesno namestio currUsera";
    }*/
}
