package com.manmatha.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manmatha.server.dto.BlogResponse;
import com.manmatha.server.dto.LogDto;
import com.manmatha.server.dto.RedgDTO;
import com.manmatha.server.security.SecurityServices;
import com.manmatha.server.service.BlogService;
import com.manmatha.server.service.CommentService;
import com.manmatha.server.service.UserService;
import com.manmatha.server.utilis.JwtUtil;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PublicController {

    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    BlogService blogService;

    @Autowired
    SecurityServices securityServices;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/")
    public ResponseEntity<?> hello() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return new ResponseEntity<>(authentication, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @PostMapping("/redg")
    public ResponseEntity<?> signUp(@RequestBody RedgDTO redgdto) {
        return userService.signUp(redgdto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogDto logDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logDto.getEmail(), logDto.getPassword()));
            UserDetails userDetails = securityServices.loadUserByUsername(authentication.getName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(jwt, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    // get all comment of a post
    // 1. blog Id need to find its comments array and return comments
    @GetMapping("/user/comments/get")
    public ResponseEntity<?> getAllCommentOfAPost(@RequestParam("blogId") Long blogId) {
        return commentService.getAllCommentOfAPost(blogId);
    }

    // get blogs
    @GetMapping("/blogs/all")
    public ResponseEntity<List<BlogResponse>> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    // get blogs by filter
    @GetMapping("/blogs/all/titel")
    public ResponseEntity<?> getAllBlogsByName(@RequestParam("titel") String titel) {
        return blogService.getAllBlogsByName(titel);
    }

    @GetMapping("/blogs/all/catagory")
    public ResponseEntity<?> getAllBlogsByCatagory(@RequestParam("catagory") String catagory) {
        return blogService.getAllBlogsByCatagory(catagory);
    }

    // get user by username
    @GetMapping("/fetch/username")
    public ResponseEntity<?> getLimitUser(@RequestParam("username") String username) {
        return userService.findByUsernameContaining(username);
    }

    // /check/username
    // get user by username
    @GetMapping("/check/username")
    public ResponseEntity<?> checkIsUser(@RequestParam("username") String username) {
        return userService.findByUsernameIsPresent(username);
    }

    @GetMapping("/fetch/demo/blog")
    public ResponseEntity<?> getDemoBlog() {
        return blogService.getAllFilteredBlogs();
    }
}
