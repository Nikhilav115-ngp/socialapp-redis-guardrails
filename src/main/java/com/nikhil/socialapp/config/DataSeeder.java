package com.nikhil.socialapp.config;

import com.nikhil.socialapp.entity.Post;
import com.nikhil.socialapp.entity.User;
import com.nikhil.socialapp.repository.PostRepository;
import com.nikhil.socialapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public DataSeeder(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) {

        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("nikhil");
            user.setPremium(false);
            userRepository.save(user);

            Post post = new Post();
            post.setContent("Hello world post");
            post.setCreatedAt(LocalDateTime.now());
            post.setLikeCount(0);
            post.setAuthor(user);
            postRepository.save(post);

            System.out.println("Sample user and post created");
        }
    }
}