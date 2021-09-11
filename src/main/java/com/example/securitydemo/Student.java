package com.example.securitydemo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Student {
    private Integer studentId;
    private String studentName;
}
