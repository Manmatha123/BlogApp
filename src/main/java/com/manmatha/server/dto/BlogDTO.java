package com.manmatha.server.dto;

import com.manmatha.server.entity.MyUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogDTO {
 
private String titel;
private String story;
private String publisheddate;
private String catagory;
MyUser myUser;
}
