package com.example.securitydemo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class StudentController {

    private final List<Student> STUDENTS = Arrays.asList(
            Student.builder().studentId(1).studentName("Harish").build(),
            Student.builder().studentId(2).studentName("Manish").build()
    );

    @GetMapping("/students/{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable("studentId") Integer studentId){
        return ResponseEntity.ok(STUDENTS
                .stream()
                .filter(s->studentId.equals(s.getStudentId()))
                .findFirst().orElseThrow(()-> new IllegalStateException()));
    }
}
