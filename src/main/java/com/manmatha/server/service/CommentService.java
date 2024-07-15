package com.manmatha.server.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.manmatha.server.dto.CommentGetDTO;
import com.manmatha.server.entity.BlogPost;
import com.manmatha.server.entity.Comment;
import com.manmatha.server.entity.MyUser;
import com.manmatha.server.repository.BlogRepo;
import com.manmatha.server.repository.CommentRepo;
import com.manmatha.server.repository.UserRepo;

@Service
public class CommentService {
@Autowired
CommentRepo commentRepo;
@Autowired
UserRepo userRepo;
@Autowired
BlogRepo blogRepo;


    public ResponseEntity<?> addComments(CommentGetDTO comment) {
        try {
          Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
      String userEmail =authentication.getName();
      MyUser mydata=userRepo.findByEmail(userEmail);
      System.out.println(comment.getEmail());
     MyUser getUser = userRepo.findByEmail(comment.getEmail());
     
      BlogPost blog = blogRepo.findById(comment.getBlogId()).get();
      BlogPost deleteBlog = blogRepo.findById(comment.getBlogId()).get();
Comment commentTest=new Comment();
commentTest.setBody(comment.getBody());
commentTest.setDate(comment.getDate()); 
commentTest.setImageuser(mydata.getProfileimg());
commentTest.setUsername(mydata.getUsername());


blog.getComments().add(commentTest);
blogRepo.save(blog);

System.out.println("test 2"+getUser.getEmail());

getUser.getBlogs().add(blog);
getUser.getBlogs().remove(deleteBlog);
userRepo.save(getUser);
System.out.println("test 3");
blogRepo.delete(deleteBlog);
System.out.println("test 4");
return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    } 
    catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }


    public ResponseEntity<?> getAllCommentOfAPost(Long blogId) {
      try {

BlogPost blog=blogRepo.findById(blogId).get();
List<Comment> comments=blog.getComments();
return new ResponseEntity<>(comments,HttpStatus.OK);
    } 
    catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

    
}
