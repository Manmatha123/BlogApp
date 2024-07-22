package com.manmatha.server.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.manmatha.server.dto.DemoUserSearchDto;
import com.manmatha.server.dto.PassDTO;
import com.manmatha.server.dto.RedgDTO;
import com.manmatha.server.dto.ResponseUser;
import com.manmatha.server.entity.BlogPost;
import com.manmatha.server.entity.Comment;
import com.manmatha.server.entity.MyUser;
import com.manmatha.server.entity.SocialMedia;
import com.manmatha.server.repository.BlogRepo;
import com.manmatha.server.repository.CommentRepo;
import com.manmatha.server.repository.SocialRepo;
import com.manmatha.server.repository.UserRepo;
import com.manmatha.server.utilis.JwtUtil;

@Service
public class UserService {
  @Autowired
  UserRepo userRepo;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  SocialRepo socialRepo;
  @Autowired
  BlogRepo blogRepo;

  @Autowired
  CommentRepo commentRepo;
  @Autowired
  JwtUtil jwtUtil;

  @Autowired
  AuthenticationManager authenticationManager;

  public ResponseEntity<?> signUp(RedgDTO myUser) {
    try {
      MyUser user = new MyUser();
      // user.setBio(myUser.getBio());
      MyUser isuser = userRepo.findByEmail(myUser.getEmail());
      if (isuser != null) {
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
      }
      user.setEmail(myUser.getEmail());
      user.setJoindate(myUser.getJoindate());
      user.setName(myUser.getName());
      user.setUsername(myUser.getUsername());
      user.setPassword(passwordEncoder.encode(myUser.getPassword()));
      userRepo.save(user);

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<?> updateUser(MyUser user) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String userEmail = authentication.getName();
      MyUser getUser = userRepo.findByEmail(userEmail);
      SocialMedia social = new SocialMedia();
      SocialMedia deleteSocial = getUser.getSocial();
      String newJwt = "";
      if (!user.getEmail().equals("") || user.getEmail() != null) {
        getUser.setEmail(user.getEmail());
        newJwt = jwtUtil.generateToken(user.getEmail());
      }
      if (!user.getName().equals("") || user.getName() != null) {
        getUser.setName(user.getName());
      }
      if (!user.getUsername().equals("") || user.getUsername() != null) {
        getUser.setUsername(user.getUsername());
      }
      if (!user.getBio().equals("") || user.getBio() != null) {
        getUser.setBio(user.getBio());
      }

      social.setFb(user.getSocial().getFb());
      social.setInsta(user.getSocial().getInsta());
      social.setLinkedin(user.getSocial().getLinkedin());
      social.setPortfolio(user.getSocial().getPortfolio());
      social.setTwit(user.getSocial().getTwit());
      social.setYt(user.getSocial().getYt());

      socialRepo.save(social);

      getUser.setSocial(social);
      userRepo.save(getUser);
      if (deleteSocial != null) {
        socialRepo.delete(deleteSocial);
      }
      return new ResponseEntity<>(newJwt, HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // uplode image /user/img
  public ResponseEntity<?> updateProfile(MultipartFile file) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String userEmail = authentication.getName();
      MyUser getUser = userRepo.findByEmail(userEmail);
      if (file.isEmpty()) {
        return new ResponseEntity<>("select image file", HttpStatusCode.valueOf(400));
      }

      if (file.getSize() > 1000000) {
        return new ResponseEntity<>("file must be less then 1mb", HttpStatusCode.valueOf(400));
      }

// String demo = new ClassPathResource("static/image").getFile().getAbsolutePath();
// System.out.println("******************");
// System.out.println(demo);
// System.out.println("******************");     
// String path=demo.replace("\\", "//").replace("target", "src").replace("classes", "main//resources");
String path="E://SpringBoot_BlogApp//server//src//main//resources//static//image";
      String pathdel = path + "//" + getUser.getProfileimg();
      File imgfile = new File(pathdel);
      imgfile.delete();
// ************change to comment****************
List<Comment> comments=commentRepo.findByUsername(getUser.getUsername());

List<BlogPost> allMyblog=getUser.getBlogs();

for(BlogPost blog:allMyblog){
  getUser.getBlogs().remove(blog);
  List<Comment>allcomments=new ArrayList<>();
for(Comment com:blog.getComments()){
  com.setImageuser(file.getOriginalFilename());
  allcomments.add(com);
}
blog.getComments().removeAll(blog.getComments());
blog.getComments().addAll(allcomments);
blogRepo.save(blog);
getUser.getBlogs().add(blog);
};

comments.forEach(comment->{
  comment.setImageuser(file.getOriginalFilename());
  commentRepo.save(comment);
});
      Files.copy(file.getInputStream(), Paths.get(path + File.separator + file.getOriginalFilename()),
          StandardCopyOption.REPLACE_EXISTING);

      getUser.setProfileimg(file.getOriginalFilename());
      userRepo.save(getUser);

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // ****get user by of its name
  public ResponseEntity<?> findByUsernameIsPresent(String username) {
    try {

       MyUser user = userRepo.findByUsername(username);
      if (user == null) {
        return new ResponseEntity<>(user,HttpStatusCode.valueOf(204));
      } else {
        return new ResponseEntity<>(HttpStatus.OK);
      }
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
// user details filter******************
public ResponseUser FilterUserdetails(MyUser user){

    ResponseUser res = new ResponseUser();
    SocialMedia socialm = new SocialMedia();

    res.setName(user.getName());
    res.setEmail(user.getEmail());
    res.setUsername(user.getUsername());
    res.setProfileimg(user.getProfileimg());
    res.setBio(user.getBio());
    res.setJoindate(user.getJoindate());

    if(user.getSocial()!=null){
      socialm.setSid(user.getSocial().getSid());
      socialm.setFb(user.getSocial().getFb());
      socialm.setInsta(user.getSocial().getInsta());
      socialm.setLinkedin(user.getSocial().getLinkedin());
      socialm.setPortfolio(user.getSocial().getPortfolio());
      socialm.setTwit(user.getSocial().getTwit());
      socialm.setYt(user.getSocial().getYt());
      res.setSocial(socialm);
    }


for(BlogPost bDto:user.getBlogs()){

BlogPost blogDTO=new BlogPost();
blogDTO.setBid(bDto.getBid());
blogDTO.setCatagory(bDto.getCatagory());
blogDTO.setPublisheddate(bDto.getPublisheddate());
blogDTO.setStory(bDto.getStory());
blogDTO.setTitel(bDto.getTitel());
blogDTO.setImg(bDto.getImg());


MyUser userDto=new MyUser();
userDto.setName(user.getName());
userDto.setEmail(user.getEmail());
userDto.setProfileimg(user.getProfileimg());
userDto.setUsername(user.getUsername());

blogDTO.setUser(userDto);

for(Comment c:bDto.getComments()){
Comment comment=new Comment();
comment.setBody(c.getBody());
comment.setCid(c.getCid());
comment.setDate(c.getDate());
comment.setImageuser(c.getImageuser());
comment.setUsername(c.getUsername());
blogDTO.getComments().add(comment);
}

blogDTO.setLikes(bDto.getLikes());
res.getBlogs().add(blogDTO);
    }
  return res;
}

// ************
  public ResponseEntity<?> getMyInfo() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      MyUser user = userRepo.findByEmail(authentication.getName());
ResponseUser responseUser=FilterUserdetails(user);

      return new ResponseEntity<>(responseUser, HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<?> updatePassword(PassDTO pass) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authentication.getName(), pass.getOldpass()));

      MyUser users = userRepo.findByEmail(authentication.getName());
      users.setPassword(passwordEncoder.encode(pass.getNewpass()));
      userRepo.save(users);

      String jwtnewtoken = jwtUtil.generateToken(authentication.getName());
      return new ResponseEntity<>(jwtnewtoken, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  public ResponseEntity<?> getUserByUserName(String username) {
    try {
      SecurityContextHolder.getContext().getAuthentication();
      MyUser users = userRepo.findByUsername(username);
      ResponseUser getFilterUser=FilterUserdetails(users);
      return new ResponseEntity<>(getFilterUser, HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
// filter out search user at home after l;ogin
public  List<DemoUserSearchDto> filterSerachUserHome(List<MyUser> users){
return users.stream().map(DemoUserSearchDto::new).toList();
}
  public ResponseEntity<?> findByUsernameContaining(String username) {
    try {
      SecurityContextHolder.getContext().getAuthentication();
      List<MyUser> users =userRepo.findByUsernameContaining(username);
      List<DemoUserSearchDto> searchdata=filterSerachUserHome(users);
System.out.println(searchdata);
      return new ResponseEntity<>(searchdata,HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

public ResponseEntity<String> geyAuthUserImg() {
  try {
  Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
 MyUser user=userRepo.findByEmail(authentication.getName());
    return new ResponseEntity<>(user.getProfileimg(),HttpStatus.OK);
  } catch (Exception e) {
    System.out.println(e);
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}



}
