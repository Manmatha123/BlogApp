package com.manmatha.server.controller;


import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manmatha.server.dto.BlogDTO;
import com.manmatha.server.dto.CommentGetDTO;
import com.manmatha.server.dto.LikeDto;
import com.manmatha.server.dto.PassDTO;
import com.manmatha.server.entity.MyUser;
import com.manmatha.server.service.BlogService;
import com.manmatha.server.service.CommentService;
import com.manmatha.server.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PrivateController {
   @Autowired
   BlogService blogService;
@Autowired
UserService userService;
@Autowired
CommentService commentService;


    @RequestMapping(value = "/blog", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBlog(@RequestPart("blogPost") BlogDTO blogPost,@RequestPart("image")MultipartFile file){
        return blogService.createBlog(blogPost,file);
    }
    // update user information
    @RequestMapping(value = "/user/img", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(@RequestPart("image") MultipartFile file){
return userService.updateProfile(file);
    }
    
    
    @PostMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody MyUser user){
        return userService.updateUser(user);
    }
    
    // update like
    @PutMapping("/like")
    public ResponseEntity<Integer> updateLike(@RequestBody LikeDto like){
        return blogService.updateLike(like);
    }

    //delete blog 
    @PutMapping("/blog/delete")
    public ResponseEntity<?>deleteBlog(@RequestParam("blogId") Long blogId){
        return blogService.deleteBlog(blogId);
    }

    // add comments to blog
    @PutMapping("/blog/comment")
    public ResponseEntity<?> addComments(@RequestBody CommentGetDTO comment){
return commentService.addComments(comment);
    }
    //user info access by user
    @GetMapping("/user/info")
    public ResponseEntity<?> getMyInfo(){
return userService.getMyInfo();
    }

    // api to get user profile image only
    @GetMapping("/user/prifileimage")
    public ResponseEntity<String> geyAuthUserImg(){
return userService.geyAuthUserImg();
    }

    //user info access by user by user name
    @GetMapping("/user/detail")
    public ResponseEntity<?> getUserByUsername(@RequestParam("username") String username){
return userService.getUserByUserName(username);
    }

    @GetMapping("/image")
public ResponseEntity<?> getImage(@RequestParam("img") String img ){
  byte[] imgByte=null;
  try{
imgByte=Files.readAllBytes(Paths.get("E://SpringBoot_BlogApp//server//src//main//resources//static//image//"+img));
  }
  catch(Exception e){
  System.out.println(e);
}
return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imgByte);
}

@GetMapping("/blogs/search")
public ResponseEntity<?> searchBlogByName(@RequestParam("titel") String titel){
    return blogService.searchBlogByName(titel);
}

@PostMapping("user/update/password")
public ResponseEntity<?> updatePassword(@RequestBody PassDTO pass){
    return userService.updatePassword(pass);
}


// update blog ie edit blog
// /api/user/blog/edit

@PutMapping("/user/blog/edit")
public ResponseEntity<?> updatePassword(@RequestBody BlogDTO blogDTO,@RequestParam("blogId") Long blogId){
    return blogService.editBlog(blogDTO,blogId);
}
}
