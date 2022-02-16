package com.ead.course.repositories;

import com.ead.course.model.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    List<LessonModel> findByModule_ModuleId(UUID id);

    Optional<LessonModel> findByLessonIdAndModule_ModuleId(UUID lessonId, UUID moduleId);
}
