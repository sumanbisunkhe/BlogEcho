package com.BlogEcho.BlogEcho.repo;

import com.BlogEcho.BlogEcho.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogId(Long blogId);
    List<Comment> findByAuthorId(Long authorId);
}
