package com.BlogEcho.BlogEcho.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Lob
    @Column(nullable = false)
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    private LocalDateTime uploadedDate;

    @Column(name = "file_path")
    private String filePath;

    public Attachment() {
    }

    @PrePersist
    protected void onCreate() {
        this.uploadedDate = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public LocalDateTime getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDateTime uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
