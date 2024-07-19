package com.BlogEcho.BlogEcho.service.impl;

import com.BlogEcho.BlogEcho.dto.BlogDto;
import com.BlogEcho.BlogEcho.exceptions.ResourceNotFoundException;
import com.BlogEcho.BlogEcho.model.Attachment;
import com.BlogEcho.BlogEcho.model.Blog;
import com.BlogEcho.BlogEcho.model.User;
import com.BlogEcho.BlogEcho.repo.BlogRepo;
import com.BlogEcho.BlogEcho.repo.UserRepo;
import com.BlogEcho.BlogEcho.service.AttachmentService;
import com.BlogEcho.BlogEcho.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepo blogRepo;
    private final UserRepo userRepo;
    private final AttachmentService attachmentService;

    @Autowired
    public BlogServiceImpl(BlogRepo blogRepo, UserRepo userRepo, AttachmentService attachmentService) {
        this.blogRepo = blogRepo;
        this.userRepo = userRepo;
        this.attachmentService = attachmentService;
    }

    @Transactional
    @Override
    public BlogDto createBlog(BlogDto blogDto) {
        User author = userRepo.findById(blogDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + blogDto.getAuthorId()));

        Blog blog = new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setContent(blogDto.getContent());
        blog.setAuthor(author);

        blog = blogRepo.save(blog);

        if (blogDto.getAttachments() != null && !blogDto.getAttachments().isEmpty()) {
            for (MultipartFile file : blogDto.getAttachments()) {
                try {
                    attachmentService.createAttachment(blog.getId(), file);
                } catch (IOException e) {
                    throw new RuntimeException("Error saving file attachment", e);
                }
            }
        }

        return convertToDto(blog);
    }

    @Override
    public BlogDto updateBlog(Long id, BlogDto blogDto) {
        Blog blog = blogRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));

        blog.setTitle(blogDto.getTitle());
        blog.setContent(blogDto.getContent());

        if (blogDto.getAttachments() != null && !blogDto.getAttachments().isEmpty()) {
            blog.clearAttachments();
            for (MultipartFile file : blogDto.getAttachments()) {
                try {
                    Attachment attachment = attachmentService.createAttachment(blog.getId(), file);
                    blog.addAttachment(attachment);
                } catch (IOException e) {
                    throw new RuntimeException("Error saving file attachment", e);
                }
            }
        }

        return convertToDto(blogRepo.save(blog));
    }



    @Override
    public List<BlogDto> getAllBlogs() {
        List<Blog> blogs = blogRepo.findAll();
        return blogs.stream()
                .map(this::convertToDto) // Convert each Blog to BlogDto
                .collect(Collectors.toList());
    }
    @Override
    public void deleteBlog(Long id) {
        blogRepo.deleteById(id);
    }

    @Override
    public Optional<Blog> findById(Long id) {
        return blogRepo.findById(id);
    }


    @Override
    public Blog save(Blog blog) {
        return blogRepo.save(blog);
    }
    @Override
    public Optional<BlogDto> getBlogById(Long id) {
        Optional<Blog> blog = blogRepo.findById(id);
        return blog.map(this::convertToDto);
    }



    private BlogDto convertToDto(Blog blog) {
        BlogDto blogDto = new BlogDto();
        blogDto.setId(blog.getId());
        blogDto.setTitle(blog.getTitle());
        blogDto.setContent(blog.getContent());
        blogDto.setAuthorId(blog.getAuthor().getId()); // Assuming you want authorId in BlogDto
        blogDto.setCreatedDate(blog.getCreatedDate());
        blogDto.setModifiedDate(blog.getModifiedDate());
        blogDto.setContent(blog.getContent());
        return blogDto;
    }

}