package com.BlogEcho.BlogEcho.dto;

import com.BlogEcho.BlogEcho.model.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class BlogDto {
    private Long id;
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    private String content;
    private Long authorId;
    @JsonFormat(pattern = "EEEE MMMM dd, yyyy HH:mm")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "EEEE MMMM dd, yyyy HH:mm")
    private LocalDateTime modifiedDate;
    private List<MultipartFile> attachments; // Example for handling attachments
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;



    // Constructors
    public BlogDto() {
    }

    public BlogDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public BlogDto(String title, String content, List<MultipartFile> attachments) {
        this.title = title;
        this.content = content;
        this.attachments = attachments;
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<MultipartFile> getAttachments() {
        return attachments;
    }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setAttachments(List<MultipartFile> attachments) {
        this.attachments = attachments;
    }
}
