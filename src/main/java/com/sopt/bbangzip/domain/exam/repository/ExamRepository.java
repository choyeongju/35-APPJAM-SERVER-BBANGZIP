package com.sopt.bbangzip.domain.exam.repository;

import com.sopt.bbangzip.domain.exam.entity.Exam;
import com.sopt.bbangzip.domain.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    /**
     * 과목 ID와 시험 이름으로 Exam 및 관련 User 조회
     */
    @Query("""
             SELECT e
             FROM Exam e
             JOIN e.subject s
             JOIN s.userSubject us
             JOIN us.user u
             WHERE s.id = :subjectId
               AND e.examName = :examName
               AND u.id = :userId
            """)
    Optional<Exam> findBySubjectIdAndExamNameAndUser(
            @Param("userId") Long userId,
            @Param("examName") String examName,
            @Param("subjectId") Long subjectId
    );

    Optional<Exam> findByExamNameAndExamDateAndSubject(String examName, LocalDate examDate, Subject subject);

    @Query("""
             SELECT e
             FROM Exam e
             JOIN e.subject s
             JOIN s.userSubject us
             JOIN us.user u
             WHERE e.examName = :examName
               AND e.examDate = :examDate
               AND s = :subject
               AND u.id = :userId
            """)
    Optional<Exam> findByExamNameAndExamDateAndSubjectAndUser(
            @Param("userId") Long userId,
            @Param("examName") String examName,
            @Param("examDate") LocalDate examDate,
            @Param("subject") Subject subject
    );
}

