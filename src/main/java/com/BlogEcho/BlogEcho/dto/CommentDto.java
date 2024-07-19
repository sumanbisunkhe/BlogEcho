package com.BlogEcho.BlogEcho.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

    private Long id;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private Long authorId;
    private Long blogId;
    @JsonFormat(pattern = "EEEE MMMM dd, yyyy HH:mm")
    private LocalDateTime createdDate;

    // Constructors
    public CommentDto() {
    }

    public CommentDto(Long id, String content, Long authorId, Long blogId, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.blogId = blogId;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
