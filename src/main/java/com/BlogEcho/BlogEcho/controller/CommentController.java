package com.BlogEcho.BlogEcho.controller;

import com.BlogEcho.BlogEcho.dto.CommentDto;
import com.BlogEcho.BlogEcho.exceptions.ResourceNotFoundException;
import com.BlogEcho.BlogEcho.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createComment(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        CommentDto createdComment = commentService.createComment(commentDto);
        Map<String, Object> response = Map.of(
                "message", "Comment created successfully.",
                "data", createdComment
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long id, @Valid @RequestBody CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        CommentDto updatedComment = commentService.updateComment(id, commentDto);
        Map<String, Object> response = Map.of(
                "message", "Comment updated successfully.",
                "data", updatedComment
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllComments() {
        List<CommentDto> comments = commentService.getAllComments();
        Map<String, Object> response = Map.of(
                "message", "Comments fetched successfully.",
                "data", comments
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCommentById(@PathVariable Long id) {
        CommentDto commentDto = commentService.getCommentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        Map<String, Object> response = Map.of(
                "message", "Comment fetched successfully.",
                "data", commentDto
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<Map<String, Object>> getCommentsByBlogId(@PathVariable Long blogId) {
        List<CommentDto> comments = commentService.getCommentsByBlogId(blogId);
        Map<String, Object> response = Map.of(
                "message", "Comments for blog ID " + blogId + " fetched successfully.",
                "data", comments
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<Map<String, Object>> getCommentsByAuthorId(@PathVariable Long authorId) {
        List<CommentDto> comments = commentService.getCommentsByAuthorId(authorId);
        Map<String, Object> response = Map.of(
                "message", "Comments by author ID " + authorId + " fetched successfully.",
                "data", comments
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        Map<String, Object> response = Map.of(
                "message", "Comment deleted successfully."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<Map<String, Object>> handleValidationErrors(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        Map<String, Object> response = Map.of(
                "message", "Validation errors",
                "errors", errors
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
