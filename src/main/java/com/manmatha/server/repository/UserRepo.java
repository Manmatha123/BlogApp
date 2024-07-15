package com.manmatha.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manmatha.server.entity.MyUser;

@Repository
public interface UserRepo extends JpaRepository<MyUser,Long>{
    
    public MyUser findByEmail(String email);

    public MyUser findByUsername(String username);

    public List<MyUser> findByUsernameContaining(String username);

}
