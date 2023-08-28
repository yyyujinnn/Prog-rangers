package com.prograngers.backend.fixture;

import com.prograngers.backend.entity.comment.Comment;
import com.prograngers.backend.entity.comment.CommentStatusConStant;
import com.prograngers.backend.entity.member.Member;
import com.prograngers.backend.entity.solution.Solution;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import static com.prograngers.backend.entity.comment.Comment.*;
import static com.prograngers.backend.entity.comment.CommentStatusConStant.*;

@AllArgsConstructor
public enum CommentFixture {

    생성된_댓글("댓글내용", CREATED),
    수정된_댓글("댓글내용", FIXED),
    삭제된_댓글("댓글내용", DELETED);

    private final String content;
    private final CommentStatusConStant status;

    public CommentBuilder 기본_빌더_생성(){
        return Comment.builder()
                .content(content)
                .status(status);
    }

    public Comment 기본_정보_댓글_생성(Member member, Solution solution, LocalDateTime createdDate){
        return 기본_빌더_생성()
                .member(member)
                .solution(solution)
                .createdDate(createdDate)
                .build();
    }
}
