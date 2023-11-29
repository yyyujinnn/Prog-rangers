package com.prograngers.backend.entity.review;

import static com.prograngers.backend.entity.review.ReviewStatusConstant.CREATED;
import static com.prograngers.backend.entity.review.ReviewStatusConstant.DELETED;
import static com.prograngers.backend.entity.review.ReviewStatusConstant.FIXED;
import static com.prograngers.backend.entity.solution.LanguageConstant.JAVA;
import static com.prograngers.backend.support.fixture.MemberFixture.장지담;
import static com.prograngers.backend.support.fixture.ProblemFixture.백준_문제;
import static com.prograngers.backend.support.fixture.ReviewFixture.생성된_리뷰;
import static com.prograngers.backend.support.fixture.SolutionFixture.공개_풀이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.prograngers.backend.entity.member.Member;
import com.prograngers.backend.entity.problem.Problem;
import com.prograngers.backend.entity.solution.Solution;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReviewTest {

    private static final String DELETED_CONTENT = "삭제된 리뷰입니다";
    private static final String FIXED_CONTENT = "수정한 댓글입니다.";
    private static final String CONTENT = "리뷰내용";
    private Review review;
    private Solution solution;
    private Member member;
    private Problem problem;

    @BeforeEach
    void beforeEach() {
        member = 장지담.기본_정보_생성();
        problem = 백준_문제.기본_정보_생성();
        solution = 공개_풀이.기본_정보_생성(problem, member, LocalDateTime.now(), JAVA, 1);
        review = 생성된_리뷰.기본_정보_생성(member, solution, LocalDateTime.now());
    }

    @DisplayName("댓글을 수정할 수 있다.")
    @Test
    void updateTest() {
        // when
        review.update(FIXED_CONTENT);

        // then
        assertAll(
                () -> assertThat(review.getContent()).isEqualTo(FIXED_CONTENT),
                () -> assertThat(review.getStatus()).isEqualTo(FIXED)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "    "})
    void 수정하려는_내용이_blank일_경우_수정할_수_없다(String content) {
        // when
        review.update(content);

        // then
        assertAll(
                () -> assertThat(review.getContent()).isEqualTo(CONTENT),
                () -> assertThat(review.getStatus()).isEqualTo(CREATED)
        );
    }

    @Test
    void 수정하려는_내용이_null일_경우_수정할_수_없다() {
        // when
        review.update(null);

        // then
        assertAll(
                () -> assertThat(review.getContent()).isEqualTo(CONTENT),
                () -> assertThat(review.getStatus()).isEqualTo(CREATED)
        );
    }

    @Test
    void 댓글을_삭제할_수_있다() {
        // when
        review.delete();

        // then
        assertAll(
                () -> assertThat(review.getContent()).isEqualTo(DELETED_CONTENT),
                () -> assertThat(review.getStatus()).isEqualTo(DELETED)
        );
    }
}