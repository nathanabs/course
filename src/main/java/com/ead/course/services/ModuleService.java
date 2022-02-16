package com.ead.course.services;

import com.ead.course.model.ModuleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    void delete(ModuleModel module);

    ModuleModel save(ModuleModel moduleModel);

    Optional<ModuleModel> findByModuleIntoCourse(UUID courseId, UUID moduleId);

    List<ModuleModel> findAllByCourseId(UUID courseId);

    Optional<ModuleModel> findById(UUID moduleId);
}