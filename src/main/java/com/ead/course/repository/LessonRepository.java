package com.ead.course.repository;

import com.ead.course.model.LessonModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    @Query(value = "select * from tb_lesson where module_module_id = :moduleId", nativeQuery = true)
    List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId);

    @Query(value = "select * from tb_Lesson where module_module_id = :moduleId and lesson_id = :lessonId", nativeQuery = true)
    Optional<LessonModel> findLessonIntoModule(@Param("moduleId") UUID moduleId, @Param("lessonId") UUID lessonId);

    @Query(value = "select * from tb_lesson where module_module_id = :moduleId", nativeQuery = true)
    List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId, Pageable pageable);
}
