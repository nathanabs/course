package com.ead.course.controller;

import com.ead.course.dto.LessonDto;
import com.ead.course.model.LessonModel;
import com.ead.course.services.LessonService;
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
public class LessonController {

    private final LessonService lessonService;
    private final ModuleService moduleService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable("moduleId" ) UUID moduleId, @RequestBody @Valid LessonDto lessonDto){
        var module = moduleService.findById(moduleId);
        if (module.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
        }

        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        lessonModel.setModule(module.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId){

        var lesson = lessonService.findByLessonIntoModule(moduleId, lessonId);
        if (lesson.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this Module.");
        }
        lessonService.delete(lesson.get());

        return ResponseEntity.ok().body("Lesson deleted successfully.");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonDto lessonDto){
        var lessonModel = lessonService.findByLessonIntoModule(moduleId, lessonId);
        if (lessonModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this Module.");
        }

        var lesson = lessonModel.get();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setDescription(lessonDto.getDescription());
        lesson.setVideoUrl(lessonDto.getVideoUrl());


        return ResponseEntity.ok(lessonService.save(lesson));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<List<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId){
        var lessons = lessonService.findAllByModulo(moduleId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getLessonById(@PathVariable(value = "moduleId") UUID moduleId,
                                                @PathVariable(value = "lessonId") UUID lessonId){
        var lessonModel = lessonService.findByLessonIntoModule(moduleId, lessonId);
        if (lessonModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this Module.");
        }


        return ResponseEntity.ok(lessonModel.get());
    }
}
