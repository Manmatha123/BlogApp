package com.manmatha.server.dto;

import java.util.ArrayList;
import java.util.List;

import com.manmatha.server.entity.Comment;
import com.manmatha.server.entity.MyUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogResponse {

    private Long bid;
    private String img;
    private String titel;
    private String story;
    private String publisheddate;
    private List<String> likes = new ArrayList<>();
    private String catagory;
    private String useremail;
    private String usename;
    private String name;
    private String userimg;

    private List<Comment> comments = new ArrayList<>();
    // @JsonIgnore
    private MyUser user;

}
