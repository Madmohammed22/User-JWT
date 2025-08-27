package com.JPA.JPA.User;

import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@Controller
public class TestController {

    @GetMapping("/list")
    List<Integer> getList(){
        return new ArrayList<>(Arrays.asList(1, 2, 3));
    }
}
