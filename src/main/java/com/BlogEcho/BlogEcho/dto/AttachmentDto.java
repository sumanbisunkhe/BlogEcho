package com.BlogEcho.BlogEcho.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachmentDto {

    private Long id;
    private String fileName;
    private String fileType;
    private byte[] data;
    private LocalDateTime uploadedDate;
    private String filePath;

    // Constructors, getters, and setters
    public AttachmentDto() {
    }

    public AttachmentDto(Long id, String fileName, String fileType, byte[] data, LocalDateTime uploadedDate, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.uploadedDate = uploadedDate;
        this.filePath = filePath;
    }

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
