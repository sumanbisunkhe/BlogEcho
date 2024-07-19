package com.BlogEcho.BlogEcho.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Content cannot be blank")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "blog", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Attachment> attachments = new HashSet<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    public Blog() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDateTime.now();
    }

    // Getters and setters
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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




    // Add attachment to blog method
    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setBlog(this); // Set the blog on the attachment
    }

    public void clearAttachments() {
        attachments.clear();
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        // Clear existing attachments
        clearAttachments();

        // Add new attachments from paths
        for (String path : attachmentPaths) {
            // Create a new Attachment object
            Attachment attachment = new Attachment();
            attachment.setFilePath(path);
            attachment.setBlog(this); // Associate with this blog

            // Add to the attachments list
            this.attachments.add(attachment);
        }
    }
}