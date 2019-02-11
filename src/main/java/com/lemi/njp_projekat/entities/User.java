package com.lemi.njp_projekat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    private String username;
    private String password;
    private String email;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}) //Promeni kasnije u merge?
    @JsonIgnore
    private List<User> following;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JsonIgnore
    private List<User> followers;
    @OneToMany (cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Post> posts;

    public void followOtherUser(User targetUser){
        if (following.contains(targetUser)){
            targetUser.follow(this);
            following.remove(targetUser);
        } else {
            targetUser.follow(this);
            following.add(targetUser);
        }

    }

    public void follow(User sourceUser){
        if (followers.contains(sourceUser)){
            followers.remove(sourceUser);
        } else {
            followers.add(sourceUser);
        }

    }

    public void addPost(Post post){
        if (!posts.contains(post)){
            post.setUser(this);
            posts.add(post);
        }
        else{
            System.out.println("Post already added");
        }
    }



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
