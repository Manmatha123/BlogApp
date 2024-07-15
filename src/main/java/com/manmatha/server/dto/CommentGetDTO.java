package com.manmatha.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentGetDTO {
    private Long blogId;
    private String email;
    private String body;
    private String date;
}
