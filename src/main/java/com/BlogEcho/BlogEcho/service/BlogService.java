package com.BlogEcho.BlogEcho.service;

import com.BlogEcho.BlogEcho.dto.BlogDto;
import com.BlogEcho.BlogEcho.model.Blog;
import java.util.List;
import java.util.Optional;

public interface BlogService {
    BlogDto createBlog(BlogDto blogDto);
    BlogDto updateBlog(Long id, BlogDto blogDto);

    Blog save(Blog blog);
    List<BlogDto> getAllBlogs();
    Optional<BlogDto> getBlogById(Long id);
//    List<BlogDto> getBlogByAuthorId(Long authorId);
//    List<BlogDto> getBlogByAuthorName(String authorName);
    void deleteBlog(Long id);

    Optional<Blog> findById(Long id);
}
