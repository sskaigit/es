package com.example.es.controller;

import com.example.es.annotation.AnnotationDemo;
import org.springframework.stereotype.Component;

@Component
public class LoginService {
    @AnnotationDemo(color = "红色")
    public String getMessge(){

        return "getMessge";
    }

    public String getService(){

        return "getService";
    }
}
