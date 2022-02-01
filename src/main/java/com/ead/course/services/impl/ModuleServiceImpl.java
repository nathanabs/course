package com.ead.course.services.impl;

import com.ead.course.model.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(ModuleModel module) {
        var lessons = lessonRepository.findByModule_ModuleId(module.getModuleId());

        if (!lessons.isEmpty()){
            lessonRepository.deleteAll(lessons);
        }
        moduleRepository.delete(module);
    }
}
