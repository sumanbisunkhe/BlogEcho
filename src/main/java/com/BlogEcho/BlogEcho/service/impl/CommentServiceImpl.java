package com.BlogEcho.BlogEcho.service.impl;

import com.BlogEcho.BlogEcho.dto.CommentDto;
import com.BlogEcho.BlogEcho.exceptions.ResourceNotFoundException;
import com.BlogEcho.BlogEcho.model.Blog;
import com.BlogEcho.BlogEcho.model.Comment;
import com.BlogEcho.BlogEcho.model.User;
import com.BlogEcho.BlogEcho.repo.BlogRepo;
import com.BlogEcho.BlogEcho.repo.CommentRepo;
import com.BlogEcho.BlogEcho.repo.UserRepo;
import com.BlogEcho.BlogEcho.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final BlogRepo blogRepo;

    @Autowired
    public CommentServiceImpl(CommentRepo commentRepo, UserRepo userRepo, BlogRepo blogRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.blogRepo = blogRepo;
    }

    @Transactional
    @Override
    public CommentDto createComment(CommentDto commentDto) {
        User author = userRepo.findById(commentDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + commentDto.getAuthorId()));

        Blog blog = blogRepo.findById(commentDto.getBlogId())
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + commentDto.getBlogId()));

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setAuthor(author);
        comment.setBlog(blog);

        return convertToDto(commentRepo.save(comment));
    }

    @Transactional
    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        comment.setContent(commentDto.getContent());

        return convertToDto(commentRepo.save(comment));
    }

    @Override
    public List<CommentDto> getAllComments() {
        return commentRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDto> getCommentById(Long id) {
        return commentRepo.findById(id).map(this::convertToDto);
    }

    @Override
    public List<CommentDto> getCommentsByBlogId(Long blogId) {
        return commentRepo.findByBlogId(blogId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByAuthorId(Long authorId) {
        return commentRepo.findByAuthorId(authorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long id) {
        commentRepo.deleteById(id);
    }

    private CommentDto convertToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getId(),
                comment.getBlog().getId(),
                comment.getCreatedDate()
        );
    }
}
