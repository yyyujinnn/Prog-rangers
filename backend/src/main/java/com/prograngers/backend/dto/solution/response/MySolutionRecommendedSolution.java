package com.prograngers.backend.dto.solution.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MySolutionRecommendedSolution {
    private Long id;
    private String solutionName;

    public static MySolutionRecommendedSolution from(Long id, String solutionName){
        return MySolutionRecommendedSolution.builder()
                .id(id)
                .solutionName(solutionName)
                .build();
    }
}
