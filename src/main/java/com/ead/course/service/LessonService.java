package com.ead.course.service;

import com.ead.course.model.LessonModel;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {
    LessonModel save(LessonModel lesson);

    Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId);

    void delete(LessonModel lessonModel);

    List<LessonModel> findAllByModule(UUID moduleId, Pageable pageable);
}
