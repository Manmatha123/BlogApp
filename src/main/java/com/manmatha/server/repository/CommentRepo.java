package com.manmatha.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.manmatha.server.entity.Comment;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Long>{

    List<Comment> findByUsername(String username);
    
}
