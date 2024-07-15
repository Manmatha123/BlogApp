package com.manmatha.server.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bid;
    private String img;
    private String titel;
    private String story;
    private String publisheddate;
    private List<String> likes = new ArrayList<>();
    private String catagory;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "blogs")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    private MyUser user;

}
