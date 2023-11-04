package com.ead.course.service;

import com.ead.course.model.CourseModel;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(CourseModel course);

    CourseModel save(CourseModel courseModel);

    Optional<CourseModel> findById(UUID courseId);

    Page<CourseModel> findAllCouses(Pageable pageable, SpecificationTemplate.CourseSpec spec);
}
