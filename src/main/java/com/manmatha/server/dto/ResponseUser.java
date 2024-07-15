package com.manmatha.server.dto;

import java.util.ArrayList;
import java.util.List;
import com.manmatha.server.entity.BlogPost;
import com.manmatha.server.entity.SocialMedia;

import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
    
    private String name;
    private String profileimg;
    private String username;
    private String email;
    private String bio;
    private String joindate;
    private SocialMedia social;

    @OneToMany
    private List<BlogPost> blogs=new ArrayList<>();

}
