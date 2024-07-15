package com.manmatha.server.dto;

import com.manmatha.server.entity.MyUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DemoUserSearchDto {
    
    private String name;
    private String profileimg;
    private String username;
    
    public DemoUserSearchDto(MyUser user){
        this.name=user.getName();
        this.profileimg=user.getProfileimg();
        this.username=user.getUsername();
    }
}
