package com.BlogEcho.BlogEcho.controller;

import com.BlogEcho.BlogEcho.dto.BlogDto;
import com.BlogEcho.BlogEcho.exceptions.ResourceNotFoundException;
import com.BlogEcho.BlogEcho.model.User;
import com.BlogEcho.BlogEcho.service.AttachmentService;
import com.BlogEcho.BlogEcho.service.BlogService;
import com.BlogEcho.BlogEcho.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;
    private final UserService userService;
    private final AttachmentService attachmentService;

    @Autowired
    public BlogController(BlogService blogService, UserService userService, AttachmentService attachmentService) {
        this.blogService = blogService;
        this.userService = userService;
        this.attachmentService = attachmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createBlog(@Valid @ModelAttribute BlogDto blogDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        // Fetch author
        User author = userService.findById(blogDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + blogDto.getAuthorId()));

        // Set authorId in BlogDto
        blogDto.setAuthorId(author.getId());

        // Create blog
        BlogDto createdBlog = blogService.createBlog(blogDto);
        Map<String, Object> response = Map.of(
                "message", "Blog created successfully.",
                "data", createdBlog
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateBlog(@PathVariable Long id, @Valid @ModelAttribute BlogDto blogDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        try {
            // Fetch existing blog and update
            BlogDto updatedBlog = blogService.updateBlog(id, blogDto);
            Map<String, Object> response = Map.of(
                    "message", "Blog updated successfully.",
                    "data", updatedBlog
            );
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            Map<String, Object> errorResponse = Map.of(
                    "message", ex.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            Map<String, Object> errorResponse = Map.of(
                    "message", "An error occurred while updating the blog."
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBlogById(@PathVariable Long id) {
        Optional<BlogDto> blogOptional = blogService.getBlogById(id);

        if (blogOptional.isPresent()) {
            Map<String, Object> response = Map.of(
                    "message", "Blog fetched successfully.",
                    "data", blogOptional.get()
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = Map.of(
                    "message", "Blog not found."
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllBlogs() {
        List<BlogDto> blogs = blogService.getAllBlogs();

        Map<String, Object> response = Map.of(
                "message", "Blogs fetched successfully.",
                "data", blogs
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteBlog(@PathVariable Long id) {
        try {
            blogService.deleteBlog(id);

            Map<String, Object> response = Map.of(
                    "message", "Blog with ID " + id + " has been successfully deleted."
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            Map<String, Object> errorResponse = Map.of(
                    "message", ex.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            Map<String, Object> errorResponse = Map.of(
                    "message", "An error occurred while deleting the blog."
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private ResponseEntity<Map<String, Object>> handleValidationErrors(BindingResult bindingResult) {
        Map<String, Object> validationErrors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));
        Map<String, Object> response = Map.of(
                "message", "Validation errors",
                "errors", validationErrors
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
