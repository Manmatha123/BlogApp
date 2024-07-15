package com.manmatha.server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manmatha.server.entity.BlogPost;

@Repository
public interface BlogRepo extends JpaRepository<BlogPost,Long>{

    List<BlogPost> findAllByCatagoryContainingIgnoreCase(String titel);

    List<BlogPost> findAllByTitelContainingIgnoreCase(String titel);


    
}
