package com.ead.course.controller;

import com.ead.course.dto.LessonDto;
import com.ead.course.model.LessonModel;
import com.ead.course.service.LessonService;
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
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    private final LessonService lessonService;
    private final ModuleService moduleService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(
            @PathVariable(value = "moduleId") UUID moduleId,
            @RequestBody @Valid LessonDto lessonDto){

        var moduleOptional = moduleService.findById(moduleId);

        if (moduleOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }

        var lesson = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lesson);
        lesson.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lesson.setModule(moduleOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lesson));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @PathVariable(value = "lessonId") UUID lessonId){

        var lessonOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

        if (lessonOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module");
        }

        lessonService.delete(lessonOptional.get());

        return ResponseEntity.ok("Lesson deleted successfully");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonDto lessonDto){
        var lessonOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

        if (lessonOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this Module");
        }

        var lesson = lessonOptional.get();

        lesson.setTitle(lessonDto.getTitle());
        lesson.setDescription(lessonDto.getDescription());
        lesson.setVideoUrl(lessonDto.getVideoUrl());

        return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lesson));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<List<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId){
        return ResponseEntity.ok(lessonService.findAllByModule(moduleId));
    }


    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId){

        var lesson = lessonService.findLessonIntoModule(moduleId, lessonId);

        if (lesson.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this Module");
        }

        return ResponseEntity.ok(lesson.get());
    }

}
