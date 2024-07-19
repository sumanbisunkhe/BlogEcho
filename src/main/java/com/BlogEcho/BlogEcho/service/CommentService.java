package com.BlogEcho.BlogEcho.service;

import com.BlogEcho.BlogEcho.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto);
    CommentDto updateComment(Long id, CommentDto commentDto);
    List<CommentDto> getAllComments();
    Optional<CommentDto> getCommentById(Long id);
    List<CommentDto> getCommentsByBlogId(Long blogId);
    List<CommentDto> getCommentsByAuthorId(Long authorId);
    void deleteComment(Long id);
}
