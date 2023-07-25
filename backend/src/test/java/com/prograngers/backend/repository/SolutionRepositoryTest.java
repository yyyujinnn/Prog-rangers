package com.prograngers.backend.repository;

import com.prograngers.backend.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;


@SpringBootTest
class SolutionRepositoryTest {

    @Autowired
    private SolutionRepository solutionRepository;

    @Test
    @Transactional
    void 정상_입력_저장_테스트(){
        // given
        Solution solution = Solution.builder()
                .level(Level.THREE)
                .title("풀이 제목")
                .algorithm(new Algorithm(null, "알고리즘명"))
                .dataStructure(new DataStructure(null, "자료구조명"))
                .code("int a=10")
                .description("풀이 설명")
                .date(LocalDate.now())
                .problem(new Problem(null, "문제", "링크", "저지명"))
                .build();

        // when
        Solution saved = solutionRepository.save(solution);

        //then
        Assertions.assertThat(saved).isEqualTo(solution);
    }



}