package com.prograngers.backend.repository;

import com.prograngers.backend.TestConfig;
import com.prograngers.backend.entity.Member;
import com.prograngers.backend.entity.Problem;
import com.prograngers.backend.entity.Solution;
import com.prograngers.backend.entity.constants.AlgorithmConstant;
import com.prograngers.backend.entity.constants.DataStructureConstant;
import com.prograngers.backend.entity.constants.LevelConstant;
import com.prograngers.backend.exception.notfound.SolutionNotFoundException;
import com.prograngers.backend.repository.problem.ProblemRepository;
import com.prograngers.backend.repository.review.QueryDslReviewRepositoryImpl;
import com.prograngers.backend.repository.solution.SolutionRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.prograngers.backend.fixture.MemberFixture.길가은1;
import static com.prograngers.backend.fixture.MemberFixture.길가은2;
import static com.prograngers.backend.fixture.ProblemFixture.문제1;
import static com.prograngers.backend.fixture.SolutionFixture.풀이1;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Slf4j
@Transactional
@Import(TestConfig.class)
class SolutionRepositoryTest {

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private EntityManager em;




    @DisplayName("풀이를 저장할 수 있다")
    @Test
    void 저장_테스트() {
        // given
        // 비영속 상태
        // id가 자동생성되기 때문에 id값 null
        Solution solution = 풀이1.기본_솔루션_생성(null);
        log.info("solution.getId() : {}",solution.getId());

        // when
        // solution이 영속 상태가 된다. saved랑 solution이랑 같은 트랜잭션 내에서 같은 id값을 가지므로 비교시 같아야 한다
        Solution saved = solutionRepository.save(solution);
        log.info("solution.getId() : {}",solution.getId());
        log.info("saved.getId() : {}",saved.getId());


        //then
        Assertions.assertThat(saved).isEqualTo(solution);
    }

    @DisplayName("풀이를 수정할 수 있다")
    @Test
    void 수정_테스트() {
        // given
        Solution solution = 풀이1.기본_솔루션_생성(null);
        Solution saved = solutionRepository.save(solution);
        saved.updateDescription("수정설명");

        // when
        Solution updated = solutionRepository.save(saved);

        // then
        Assertions.assertThat(updated).isEqualTo(saved);
    }

    @DisplayName("풀이를 삭제할 수 있다")
    @Test
    void 삭제_테스트() {
        // given
        Solution solution = 풀이1.기본_솔루션_생성(null);
        Solution saved = solutionRepository.save(solution);
        log.info("saved id : {}", saved.getId());

        // when
        solutionRepository.delete(solution);

        // then
        org.junit.jupiter.api.Assertions.assertThrows(SolutionNotFoundException.class,
                () -> {
                    solutionRepository.findById(solution.getId()).orElseThrow(() -> new SolutionNotFoundException());
                });
    }

    @DisplayName("멤버 이름으로 풀이를 전부 찾을 수 있다")
    @Test
    void 멤버_이름으로_전부_찾기_테스트(){

        // given
        Member member1 = 길가은1.아이디_값_지정_멤버_생성(null);
        Member member2 = 길가은2.아이디_값_지정_멤버_생성(null);
        Problem problem = 문제1.아이디_값_지정_문제_생성(null);
        // problem은 solution이 저장될 때 같이 저장된다, member는 solution과 cascade 옵션이 걸려있지 않다
        em.persist(member1);
        em.persist(member2);
        Solution solution1 = 풀이1.일반_솔루션_생성(null, problem, member1, 0, AlgorithmConstant.BFS, DataStructureConstant.ARRAY);
        Solution solution2 = 풀이1.일반_솔루션_생성(null, problem, member1, 0, AlgorithmConstant.BFS, DataStructureConstant.ARRAY);
        Solution solution3 = 풀이1.일반_솔루션_생성(null, problem, member2, 0, AlgorithmConstant.BFS, DataStructureConstant.ARRAY);
        solutionRepository.save(solution1);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);

        // when
        List<Solution> result = solutionRepository.findAllByMember(member1);

        // then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}