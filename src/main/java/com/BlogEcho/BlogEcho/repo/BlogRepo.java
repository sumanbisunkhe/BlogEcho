package com.BlogEcho.BlogEcho.repo;

import com.BlogEcho.BlogEcho.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepo extends JpaRepository<Blog, Long> {
    List<Blog> findByAuthorId(Long authorId);

    List<Blog> findByAuthorUsername(String authorName);
}