package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.CommentRequestDto;
import com.sparta.reviewspotproject.dto.CommentResponseDto;
import com.sparta.reviewspotproject.entity.Comment;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.repository.CommentRepository;
import com.sparta.reviewspotproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 댓글 생성
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = findPostById(postId);
        Comment comment = commentRepository.save(new Comment(requestDto, post, user));
        return new CommentResponseDto(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        Comment comment = findCommentById(id);
        if (comment.getUser().getId() == user.getId()) {
            comment.update(requestDto);
            return new CommentResponseDto(comment);
        } else throw new IllegalArgumentException("본인이 작성한 댓글만 수정할 수 있습니다.");
    }

    // 댓글 삭제
    public void deleteComment(Long id, User user) {
        Comment comment = findCommentById(id);
        if (comment.getUser().getId() == user.getId()) {
            commentRepository.delete(comment);
        } else throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
    }

    // 댓글 전체조회
    public List<CommentResponseDto> getAllComment(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        List<CommentResponseDto> commentAllList = new ArrayList<>();
        for (Comment commentResponseDto : comments) {
            commentAllList.add(new CommentResponseDto(commentResponseDto));
        }
        return commentAllList;
    }

    // id 존재 확인 메서드
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
    }

    // 일정 id 존재 확인 메서드
    private Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 스케줄을 찾을 수 없습니다."));
    }
}
