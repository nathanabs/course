package com.ead.course.controller;

import com.ead.course.dto.CourseDto;
import com.ead.course.model.CourseModel;
import com.ead.course.service.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto){

        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> saveCourse(@PathVariable(value = "courseId") UUID courseId){

        var courseModel = courseService.findById(courseId);

        if (courseModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        courseService.delete(courseModel.get());

        return ResponseEntity.ok("Course deleted successfully");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId,
                                               @RequestBody @Valid CourseDto courseDto){
        var courseOptional = courseService.findById(courseId);

        if (courseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        var course = courseOptional.get();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setImageUrl(courseDto.getImageUrl());
        course.setCourseStatus(courseDto.getCourseStatus());
        course.setCourseLevel(courseDto.getCourseLevel());
        course.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(course));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(
            SpecificationTemplate.CourseSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(courseService.findAllCouses(pageable, spec));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId){
        var courseOptional = courseService.findById(courseId);

        if (courseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        return ResponseEntity.ok(courseOptional.get());
    }
}
