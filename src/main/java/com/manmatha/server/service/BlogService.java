package com.manmatha.server.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.manmatha.server.dto.BlogDTO;
import com.manmatha.server.dto.BlogResponse;
import com.manmatha.server.dto.BlogUnAuthDto;
import com.manmatha.server.dto.LikeDto;
import com.manmatha.server.entity.BlogPost;
import com.manmatha.server.entity.Comment;
import com.manmatha.server.entity.MyUser;
import com.manmatha.server.repository.BlogRepo;
import com.manmatha.server.repository.UserRepo;

@Service
public class BlogService {

  @Autowired
  BlogRepo blogRepo;
  @Autowired
  UserRepo userRepo;

  public ResponseEntity<?> createBlog(BlogDTO blogPost, MultipartFile file) {
    try {
      BlogPost blog = new BlogPost();
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String userEmail = authentication.getName();
      MyUser getUser = userRepo.findByEmail(userEmail);

      // ****************
      if (file.getSize() > 1000000) {
        return new ResponseEntity<>("File size should be less then 1MB", HttpStatus.BAD_REQUEST);
      }
      blog.setImg(file.getOriginalFilename());
     

String demo = new ClassPathResource("static/image").getFile().getAbsolutePath();
String path=demo.replace("\\", "//").replace("target", "src").replace("classes", "main//resources");

      Files.copy(file.getInputStream(), Paths.get(path + File.separator + file.getOriginalFilename()),
          StandardCopyOption.REPLACE_EXISTING);

      // ************** */
      // blog.setUseremail(userEmail);
      blog.setCatagory(blogPost.getCatagory());
      blog.setStory(blogPost.getStory());
      blog.setTitel(blogPost.getTitel());
      blog.setPublisheddate(blogPost.getPublisheddate());

      blog.setUser(getUser);
      blogRepo.save(blog);

      getUser.getBlogs().add(blog);
      userRepo.save(getUser);
      return new ResponseEntity<>(HttpStatus.OK);

    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // update llike
  public ResponseEntity<Integer> updateLike(LikeDto like) {
    try {

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      // BlogPost blog=new BlogPost();
      String myEmail = authentication.getName();
      String userEmail = like.getEmail();
      MyUser getUser = userRepo.findByEmail(userEmail);
      BlogPost blogone = blogRepo.findById(like.getLikeId()).get();
      BlogPost deleteBlog = blogRepo.findById(like.getLikeId()).get();

      if (blogone.getLikes().contains(myEmail)) {
        blogone.getLikes().remove(myEmail);
        blogRepo.save(blogone);
        getUser.getBlogs().remove(deleteBlog);
        getUser.getBlogs().add(blogone);
        userRepo.save(getUser);
        blogRepo.delete(deleteBlog);
        return new ResponseEntity<>(blogone.getLikes().size(), HttpStatus.OK);
      }

      blogone.getLikes().add(myEmail);
      blogRepo.save(blogone);
      getUser.getBlogs().remove(deleteBlog);
      getUser.getBlogs().add(blogone);
      userRepo.save(getUser);
      blogRepo.delete(deleteBlog);

      return new ResponseEntity<>(blogone.getLikes().size(), HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // delete blog
  public ResponseEntity<?> deleteBlog(Long blogId) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String userEmail = authentication.getName();
      MyUser getUser = userRepo.findByEmail(userEmail);
      BlogPost blog = blogRepo.findById(blogId).get();
      BlogPost deleteBlog = blogRepo.findById(blogId).get();
      blogRepo.delete(blog);

      String demo = new ClassPathResource("static/image").getFile().getAbsolutePath();
      String path=demo.replace("\\", "//").replace("target", "src").replace("classes", "main//resources")+"//"+deleteBlog.getImg();
      File imgfile = new File(path);
      imgfile.delete();

      getUser.getBlogs().remove(deleteBlog);
      userRepo.save(getUser);

      return new ResponseEntity<>(HttpStatusCode.valueOf(204));

    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // *************************************
  public List<BlogResponse> FilterUserdetails(List<BlogPost> blogs) {

    List<BlogResponse> blogResponses = new ArrayList<>();

    blogs.forEach(blog -> {

      BlogResponse bg = new BlogResponse();
      bg.setBid(blog.getBid());
      bg.setCatagory(blog.getCatagory());
      bg.setImg(blog.getImg());
      bg.setTitel(blog.getTitel());
      bg.setStory(blog.getStory());
      bg.setPublisheddate(blog.getPublisheddate());

      for (Comment c : blog.getComments()) {
        Comment comment = new Comment();
        comment.setBody(c.getBody());
        comment.setCid(c.getCid());
        comment.setDate(c.getDate());
        comment.setImageuser(c.getImageuser());
        comment.setUsername(c.getUsername());
        bg.getComments().add(comment);
      }
      bg.setLikes(blog.getLikes());

      bg.setName(blog.getUser().getName());
      bg.setUseremail(blog.getUser().getEmail());
      bg.setUsename(blog.getUser().getUsername());
      bg.setUserimg(blog.getUser().getProfileimg());
      blogResponses.add(bg);
    });

    return blogResponses;
  }

  // ********************************
  // all blog get
  public ResponseEntity<List<BlogResponse>> getAllBlogs() {
    try {
      List<BlogPost> allblog = blogRepo.findAll();

      List<BlogResponse> getFilterBlog = FilterUserdetails(allblog);
      return new ResponseEntity<>(getFilterBlog, HttpStatusCode.valueOf(200));

    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<?> searchBlogByName(String titel) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String userMail = authentication.getName();
      List<BlogPost>allblog=blogRepo.findAllByTitelContainingIgnoreCase(titel);
List<BlogPost> filterblog=allblog.stream().filter(obj->obj.getUser().getEmail().equals(userMail)).collect(Collectors.toList());

      return new ResponseEntity<>(filterblog, HttpStatusCode.valueOf(200));
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // edit blog by auth user
  public ResponseEntity<?> editBlog(BlogDTO blogDTO, Long blogId) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      MyUser user = userRepo.findByEmail(authentication.getName());

      BlogPost blog = blogRepo.findById(blogId).get();
      BlogPost blogDel = blogRepo.findById(blogId).get();

      blog.setCatagory(blogDTO.getCatagory());
      blog.setTitel(blogDTO.getTitel());
      blog.setStory(blogDTO.getStory());
      blogRepo.save(blog);

      user.getBlogs().add(blog);
      user.getBlogs().remove(blogDel);
      userRepo.save(user);
      blogRepo.delete(blogDel);

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<List<BlogResponse>> getAllBlogsByName(String titel) {
    List<BlogPost> allblog = blogRepo.findAllByTitelContainingIgnoreCase(titel);
    List<BlogResponse> getFilterBlog = FilterUserdetails(allblog);
    return new ResponseEntity<>(getFilterBlog, HttpStatusCode.valueOf(200));
  }

  public ResponseEntity<?> getAllBlogsByCatagory(String catagory) {
    List<BlogPost> allblog = blogRepo.findAllByCatagoryContainingIgnoreCase(catagory);
    List<BlogResponse> getFilterBlog = FilterUserdetails(allblog);
    return new ResponseEntity<>(getFilterBlog, HttpStatusCode.valueOf(200));
  }

  // *****************show blog for unauth user******************
  public List<BlogUnAuthDto> filterUnauthUser(List<BlogPost> blogs) {
    List<BlogUnAuthDto> blogUnAuthDto=new ArrayList<>();
    try {
     
     blogs.forEach((blog)->{
      BlogUnAuthDto blogObj=new BlogUnAuthDto();
      blogObj.setImg(blog.getImg());
      blogObj.setPublisheddate(blog.getPublisheddate());
      blogObj.setStory(blog.getStory());
      blogObj.setTitel(blog.getTitel());
      blogObj.setLikesno(blog.getLikes().size());
      blogObj.setCommentno(blog.getComments().size());

     MyUser user= blog.getUser();
      blogObj.setUserimg(user.getProfileimg());
      blogObj.setName(user.getName());
      blogObj.setUsename(user.getUsername());
      blogUnAuthDto.add(blogObj);
     });
      return blogUnAuthDto;
    } catch (Exception e) {
      System.out.println(e);
      return blogUnAuthDto;
    }

  }

  public ResponseEntity<?> getAllFilteredBlogs() {
    try {
      List<BlogPost> allblogs = blogRepo.findAll();
      List<BlogUnAuthDto> getBlog=filterUnauthUser(allblogs);
      return new ResponseEntity<>(getBlog,HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
