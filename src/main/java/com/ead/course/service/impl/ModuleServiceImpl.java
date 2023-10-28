package com.ead.course.service.impl;

import com.ead.course.model.ModuleModel;
import com.ead.course.repository.LessonRepository;
import com.ead.course.repository.ModuleRepository;
import com.ead.course.service.ModuleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(ModuleModel module) {
        var lessons = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
        if (!lessons.isEmpty()) {
            lessonRepository.deleteAll(lessons);
        }

        moduleRepository.delete(module);
    }
}


