package com.manmatha.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogUnAuthDto {

    private String img;
    private String titel;
    private String story;
    private String publisheddate;
    private Number likesno;
    private Number commentno;
    private String usename;
    private String name;
    private String userimg;
}
