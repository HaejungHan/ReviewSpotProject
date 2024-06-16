package com.sparta.reviewspotproject.repository;

import com.sparta.reviewspotproject.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPostId(Long postId);
}

