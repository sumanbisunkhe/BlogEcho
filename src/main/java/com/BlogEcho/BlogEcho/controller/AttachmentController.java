package com.BlogEcho.BlogEcho.controller;

import com.BlogEcho.BlogEcho.dto.AttachmentDto;
import com.BlogEcho.BlogEcho.model.Attachment;
import com.BlogEcho.BlogEcho.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAttachmentById(@PathVariable Long id) {
        Optional<Attachment> attachment = attachmentService.getAttachmentById(id);
        if (attachment.isPresent()) {
            Map<String, Object> response = Map.of(
                    "message", "Attachment fetched successfully",
                    "data", attachment.get()
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = Map.of(
                    "message", "Attachment not found"
            );
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<Map<String, Object>> getAttachmentsByBlogId(@PathVariable Long blogId) {
        List<Attachment> attachments = attachmentService.getAttachmentByBlogId(blogId);
        Map<String, Object> response = Map.of(
                "message", "Attachments fetched successfully",
                "data", attachments
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<Map<String, Object>> getAttachmentsByAuthorId(@PathVariable Long authorId) {
        List<Attachment> attachments = attachmentService.getAttachmentByAuthorId(authorId);
        Map<String, Object> response = Map.of(
                "message", "Attachments fetched successfully",
                "data", attachments
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllAttachments() {
        List<Attachment> attachments = attachmentService.getAllAttachments();
        Map<String, Object> response = Map.of(
                "message", "All Attachments fetched successfully",
                "data", attachments
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAttachment(
            @PathVariable Long id,
            @RequestBody AttachmentDto attachmentDto) {
        Attachment updatedAttachment = attachmentService.updateAttachment(id, attachmentDto);
        Map<String, Object> response = Map.of(
                "message", "Attachment updated successfully",
                "data", updatedAttachment
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAttachment(@PathVariable Long id) {
        attachmentService.deleteAttachment(id);
        Map<String, Object> response = Map.of(
                "message", "Attachment with ID " + id + " has been deleted"
        );
        return ResponseEntity.ok(response);
    }
}
