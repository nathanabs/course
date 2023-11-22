package com.ead.course.service;

import com.ead.course.model.ModuleModel;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {
    void delete(ModuleModel module);

    ModuleModel save(ModuleModel moduleModel);

    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

    List<ModuleModel> findAllModulesByCourse(UUID courseId, Pageable pageable);

    Optional<ModuleModel> findById(UUID moduleId);
}
