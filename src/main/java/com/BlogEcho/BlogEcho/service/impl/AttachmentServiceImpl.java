package com.BlogEcho.BlogEcho.service.impl;

import com.BlogEcho.BlogEcho.dto.AttachmentDto;
import com.BlogEcho.BlogEcho.exceptions.ResourceNotFoundException;
import com.BlogEcho.BlogEcho.model.Attachment;
import com.BlogEcho.BlogEcho.model.Blog;
import com.BlogEcho.BlogEcho.repo.AttachmentRepo;
import com.BlogEcho.BlogEcho.repo.BlogRepo;
import com.BlogEcho.BlogEcho.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepo attachmentRepo;
    private final BlogRepo blogRepo; // Inject BlogRepo to fetch Blog by ID

    @Autowired
    public AttachmentServiceImpl(AttachmentRepo attachmentRepo, BlogRepo blogRepo) {
        this.attachmentRepo = attachmentRepo;
        this.blogRepo = blogRepo;
    }

    @Override
    public Attachment createAttachment(Long blogId, MultipartFile file) throws IOException {
        Blog blog = blogRepo.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + blogId));

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setData(file.getBytes());
        attachment.setUploadedDate(LocalDateTime.now());
        attachment.setFilePath(saveFile(file));
        attachment.setBlog(blog); // Set the Blog reference

        return attachmentRepo.save(attachment);
    }
    private String saveFile(MultipartFile file) throws IOException {
        // Adjust the directory path where you want to save the file
        String directoryPath = "D:\\JAVA\\BlogEcho Files";

        // Example: save to local directory and return file path
        String filePath = directoryPath + file.getOriginalFilename();
        File dest = new File(filePath);
        file.transferTo(dest);

        return filePath; // Return the saved file path
    }

    @Override
    public Optional<Attachment> getAttachmentById(Long id) {
        return attachmentRepo.findById(id);
    }

    @Override
    public List<Attachment> getAttachmentByBlogId(Long blogId) {
        return attachmentRepo.findByBlogId(blogId);
    }

    @Override
    public List<Attachment> getAttachmentByAuthorId(Long authorId) {
        return attachmentRepo.findByBlogAuthorId(authorId);
    }

    @Override
    public List<Attachment> getAllAttachments() {
        return attachmentRepo.findAll();
    }

    @Override
    public Attachment updateAttachment(Long id, AttachmentDto attachmentDto) {
        Attachment attachment = attachmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id: " + id));

        attachment.setFileName(attachmentDto.getFileName());
        attachment.setFileType(attachmentDto.getFileType());
        attachment.setData(attachmentDto.getData());
        attachment.setUploadedDate(LocalDateTime.now());
        attachment.setFilePath(attachmentDto.getFilePath());

        return attachmentRepo.save(attachment);
    }

    @Override
    public void deleteAttachment(Long id) {
        attachmentRepo.deleteById(id);
    }
    @Override
    public List<Attachment> getAttachmentsByAuthorName(String authorName) {
        return attachmentRepo.findByBlogAuthorUsername(authorName);
    }

    @Override
    public List<Attachment> getAllAttachmentsSortedBySizeDescending() {
        return attachmentRepo.findAllByOrderByDataDesc();
    }

    @Override
    public List<Attachment> getAllAttachmentsSortedByAuthorNameAlphabetically() {
        return attachmentRepo.findAllByOrderByBlogAuthorUsernameAsc();
    }

    @Override
    public List<Attachment> getAllAttachmentsSortedByTimeDescending() {
        return attachmentRepo.findAllByOrderByUploadedDateDesc();
    }


}
