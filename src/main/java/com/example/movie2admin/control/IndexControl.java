package com.example.movie2admin.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexControl {
    @GetMapping(value = "/")
    public String index(){
        return "index";
    }
}
