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

    private boolean mine;
    private static final String SCRAP_PATH = "http://localhost:8080/prog-rangers/solutions/";

    public static SolutionDetailResponse from(Problem problem, Solution solution, List<Comment> comments,
                                                  boolean scraped, int scrapCount, boolean pushedLike, int likeCount, boolean isMine) {
        SolutionDetailResponse response = new SolutionDetailResponse();
        String scrapLink = getScrapLink(solution);
        SolutionDetailSolution responseSolution = getResponseSolution(solution, scraped, scrapCount, pushedLike, likeCount, scrapLink);
        List<SolutionDetailComment> commentResponseList = new ArrayList<>();
        addCommentsToResponse(comments, commentResponseList);
        setData(problem, isMine, response, responseSolution, commentResponseList);
        return response;
    }

    private static void setData(Problem problem, boolean isMine, SolutionDetailResponse response, SolutionDetailSolution responseSolution, List<SolutionDetailComment> commentResponseList) {
        response.setComments(commentResponseList);
        response.setSolution(responseSolution);
        response.setMine(isMine);
        response.setProblem(SolutionDetailProblem.from(problem));
    }

    private static void addCommentsToResponse(List<Comment> comments, List<SolutionDetailComment> commentResponseList) {
        // 먼저 부모가 없는 댓글들을 전부 더한다
        comments.stream().filter(comment -> comment.getParentId()==null)
                .forEach(comment-> commentResponseList.add( SolutionDetailComment.from(comment)));
        // 부모가 있는 댓글들을 더한다
        comments.stream().filter((comment)->comment.getParentId()!=null)
                .forEach((comment)->{
                    for (SolutionDetailComment parentComment : commentResponseList){
                        if (parentComment.getId().equals(comment.getParentId())){
                            parentComment.getReplies().add(SolutionDetailComment.from(comment));
                        }
                    }
                });
    }

    private static SolutionDetailSolution getResponseSolution(Solution solution, boolean scraped, int scrapCount, boolean pushedLike, int likeCount, String scrapLink) {
        SolutionDetailSolution responseSolution = new SolutionDetailSolution(
                solution.getId(),
                solution.getMember().getNickname(),
                solution.getTitle(),
                solution.getProblem().getLink(),
                solution.getAlgorithm(),
                solution.getDataStructure(),
                solution.getCode().split("\n"),
                solution.getDescription(),
                likeCount,
                scrapCount,
                scrapLink,
                pushedLike,
                scraped
        );
        return responseSolution;
    }

    private static String getScrapLink(Solution solution) {
        // 스크랩 한 풀이면 스크랩 한 풀이의 링크 찾기
        String scrapLink = null;
        if (solution.getScrapSolution() != null) {
            scrapLink = SCRAP_PATH + solution.getScrapSolution().getId();
        }
        return scrapLink;
    }
}
