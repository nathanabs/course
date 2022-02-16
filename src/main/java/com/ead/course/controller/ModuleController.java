package com.ead.course.controller;

import com.ead.course.dto.ModuleDto;
import com.ead.course.model.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    private final ModuleService moduleService;
    private final CourseService courseService;

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable("courseId" ) UUID courseId, @RequestBody @Valid ModuleDto moduleDto){
        var course = courseService.findById(courseId);
        if (course.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        moduleModel.setCourse(course.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }

    @DeleteMapping("/courses/{courseId}/modules{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId){

        var moduleModel = moduleService.findByModuleIntoCourse(courseId, moduleId);
        if (moduleModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Modulo not found for this course.");
        }
        moduleService.delete(moduleModel.get());

        return ResponseEntity.ok().body("Module deleted successfully.");
    }

    @PutMapping("/courses/{courseId}/modules{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto){
        var moduleModel = moduleService.findByModuleIntoCourse(courseId, moduleId);
        if (moduleModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Modulo not found for this course.");
        }

        var module = moduleModel.get();
        module.setTitle(moduleDto.getTitle());
        module.setDescription(moduleDto.getDescription());


        return ResponseEntity.ok(moduleService.save(module));
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId){
        var courses = moduleService.findAllByCourseId(courseId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getModuloById(@PathVariable(value = "courseId") UUID courseId,
                                                @PathVariable(value = "moduleId") UUID moduleId){
        var moduleModel = moduleService.findByModuleIntoCourse(courseId, moduleId);
        if (moduleModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }


        return ResponseEntity.ok(moduleModel.get());
    }
}
