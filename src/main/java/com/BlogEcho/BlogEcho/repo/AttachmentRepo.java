package com.BlogEcho.BlogEcho.repo;

import com.BlogEcho.BlogEcho.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepo extends JpaRepository<Attachment, Long> {
    List<Attachment> findByBlogId(Long blogId);

    List<Attachment> findByBlogAuthorId(Long authorId);

    List<Attachment> findByBlogAuthorUsername(String authorName);

    List<Attachment> findAllByOrderByDataDesc();

    List<Attachment> findAllByOrderByBlogAuthorUsernameAsc();

    List<Attachment> findAllByOrderByUploadedDateDesc();
}
