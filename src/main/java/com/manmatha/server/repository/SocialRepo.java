package com.manmatha.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manmatha.server.entity.SocialMedia;

@Repository
public interface SocialRepo extends JpaRepository<SocialMedia,Long>{
    
}
