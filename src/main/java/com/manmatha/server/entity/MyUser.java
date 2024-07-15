package com.manmatha.server.entity;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uid;
    private String name;
    private String profileimg;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String bio;
    private String joindate;
    @JsonIgnore
    private String role = "USER";

    @OneToOne(cascade = CascadeType.ALL)
    private SocialMedia social;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    // @JsonManagedReference
    private List<BlogPost> blogs = new ArrayList<>();

}
