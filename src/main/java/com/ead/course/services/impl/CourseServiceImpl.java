package com.ead.course.services.impl;

import com.ead.course.model.CourseModel;
import com.ead.course.model.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    @Override
    public void deleteCourse(CourseModel course) {
        var listaModule = moduleRepository.findAllModulesIntoCourse(course.getCourseId());

        removerLessonsDosModule(listaModule);
        removerModuleDosCourses(listaModule);
        courseRepository.delete(course);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }

    private void removerLessonsDosModule(List<ModuleModel> listaModule) {
        if (!listaModule.isEmpty()){
            for (ModuleModel module : listaModule){
                var listaLesson = lessonRepository.findByModule_ModuleId(module.getModuleId());
                if (!listaLesson.isEmpty()){
                    lessonRepository.deleteAll(listaLesson);
                }
            }
        }
    }

    private void removerModuleDosCourses(List<ModuleModel> listaModule) {
        if (!listaModule.isEmpty()){
            moduleRepository.deleteAll(listaModule);
        }
    }
}
