package com.BlogEcho.BlogEcho.service;

import com.BlogEcho.BlogEcho.dto.AttachmentDto;
import com.BlogEcho.BlogEcho.model.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AttachmentService {

    Attachment createAttachment(Long blogId, MultipartFile file) throws IOException;

    Optional<Attachment> getAttachmentById(Long id);

    List<Attachment> getAttachmentByBlogId(Long blogId);

    List<Attachment> getAttachmentByAuthorId(Long authorId);

    List<Attachment> getAllAttachments();

    Attachment updateAttachment(Long id, AttachmentDto attachmentDto);

    void deleteAttachment(Long id);

    List<Attachment> getAttachmentsByAuthorName(String authorName);

    List<Attachment> getAllAttachmentsSortedBySizeDescending();

    List<Attachment> getAllAttachmentsSortedByAuthorNameAlphabetically();

    List<Attachment> getAllAttachmentsSortedByTimeDescending();
}

