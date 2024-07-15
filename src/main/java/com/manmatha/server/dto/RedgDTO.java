package com.manmatha.server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RedgDTO {
    
    private String name;
    private String email;
    private String password;
    private String username;
    private String joindate;
}
