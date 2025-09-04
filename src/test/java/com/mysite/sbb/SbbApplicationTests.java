package com.mysite.sbb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void clean() {
        questionRepository.deleteAll();
    }

    private Question saveQ(String subject, String content) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        return questionRepository.save(q);
    }

    @Test
    void questionFindAllTest() {
        saveQ("스프링부트 모델 질문입니다.", "내용1");
        saveQ("sbb가 무엇인가요?", "내용2");

        List<Question> all = questionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        assertEquals(2, all.size());
        assertEquals("스프링부트 모델 질문입니다.", all.get(0).getSubject()); // 정렬 고정
    }

    @Test
    void questionFindByIdTest() {
        Question saved = saveQ("id로 찾기", "내용");
        Question found = questionRepository.findById(saved.getId()).orElseThrow();
        assertEquals("id로 찾기", found.getSubject());
    }

    @Test
    void questionFindBySubjectTest() {
        Question saved = saveQ("sbb가 무엇인가요?", "내용");
        // 리포지토리 시그니처에 맞게 사용 (Optional이면 .orElseThrow())
        Question found = questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(saved.getId(), found.getId());
    }
}
