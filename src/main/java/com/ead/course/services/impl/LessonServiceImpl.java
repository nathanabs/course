package com.ead.course.services.impl;

import com.ead.course.model.LessonModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository repository;

    @Override
    public LessonModel save(LessonModel lessonModel) {
        return repository.save(lessonModel);
    }

    @Override
    public Optional<LessonModel> findByLessonIntoModule(UUID moduleId, UUID lessonId) {
        return repository.findByLessonIdAndModule_ModuleId(lessonId, moduleId);
    }

    @Override
    public void delete(LessonModel lessonModel) {
        repository.delete(lessonModel);
    }

    @Override
    public List<LessonModel> findAllByModulo(UUID moduleId) {
        return repository.findByModule_ModuleId(moduleId);
    }
}
