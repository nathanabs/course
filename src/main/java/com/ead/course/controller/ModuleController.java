package com.ead.course.controller;

import com.ead.course.dto.ModuleDto;
import com.ead.course.model.ModuleModel;
import com.ead.course.service.CourseService;
import com.ead.course.service.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;
    private final CourseService courseService;

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid ModuleDto moduleDto){

        var courseOptional = courseService.findById(courseId);

        if (courseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> saveCourse(@PathVariable(value = "courseId") UUID courseId,
                                             @PathVariable(value = "moduleId") UUID moduleId){

        var moduleOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (moduleOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course");
        }

        moduleService.delete(moduleOptional.get());

        return ResponseEntity.ok("Module deleted successfully");
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto){
        var moduleOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (moduleOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course");
        }

        var moduleModel = moduleOptional.get();

        moduleModel.setTitle(moduleDto.getTitle());
        moduleModel.setDescription(moduleDto.getDescription());

        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId){
        return ResponseEntity.ok(moduleService.findAllModulesByCourse(courseId));
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId){

        var moduleOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (moduleOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course");
        }

        return ResponseEntity.ok(moduleOptional.get());
    }
}