package com.prograngers.backend.dto.solution.response;

import com.prograngers.backend.entity.comment.Comment;
import com.prograngers.backend.entity.problem.Problem;
import com.prograngers.backend.entity.solution.Solution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SolutionDetailResponse {

    private SolutionDetailProblem problem;
    private SolutionDetailSolution solution;
    private List<SolutionDetailComment> comments;

    private boolean isMine;
    private static final String SCRAP_PATH = "http://localhost:8080/prog-rangers/solutions/";

    public static SolutionDetailResponse from(Problem problem, Solution solution, List<Comment> comments,
                                                  boolean scraped, int scrapCount,
                                                  boolean pushedLike, int likeCount,
                                                  boolean isMine
    ) {


        SolutionDetailResponse response = new SolutionDetailResponse();



        // 스크랩 한 풀이면 스크랩 한 풀이의 링크 찾기
        String scrapLink = null;
        if (solution.getScrapSolution() != null) {
            scrapLink = SCRAP_PATH + solution.getScrapSolution().getId();
        }

        SolutionDetailSolution responseSolution = new SolutionDetailSolution(
                solution.getId(),
                solution.getMember().getNickname(),
                solution.getTitle(),
                solution.getProblem().getLink(),
                solution.getAlgorithm(),
                solution.getDataStructure(),
                solution.getCode(),
                solution.getDescription(),
                likeCount,
                scrapCount,
                scrapLink,
                pushedLike,
                scraped
        );

        List<SolutionDetailComment> commentResponseList = new ArrayList<>();

        // 먼저 부모가 없는 댓글들을 전부 더한다
        comments.stream().filter(comment -> comment.getParentId()==null)
                .forEach(comment->commentResponseList.add( new SolutionDetailComment(
                        comment.getMember().getPhoto(),
                        comment.getId(),
                        comment.getMember().getNickname(),
                        comment.getContent(),
                        comment.getMention(),
                        comment.getStatus(),
                        new ArrayList<>()
                )));

        log.info("부모가 있는 댓글 더하기 전");
        // 부모가 있는 댓글들을 더한다
        for (Comment comment : comments){
            Long parentId = comment.getParentId();
            if (parentId!=null){
                for (SolutionDetailComment parentComment : commentResponseList){
                    if (parentComment.getId().equals(parentId)){
                        parentComment.getReplies().add(
                                new SolutionDetailComment(
                                        comment.getMember().getPhoto(),
                                        comment.getId(),
                                        comment.getMember().getNickname(),
                                        comment.getContent(),
                                        comment.getMention(),
                                        comment.getStatus(),
                                        null
                                )
                        );
                    }
                }
            }
        }
        log.info("부모가 있는 댓글 더하기 후");

        response.comments = commentResponseList;
        response.solution = responseSolution;
        response.isMine = isMine;
        response.problem= SolutionDetailProblem.from(problem);

        return response;
    }
}