package com.example.hello;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hello.user.User;

@RestController
public class HelloController {

    private final CrudTestRepository crudTestRepository;

    HelloController(CrudTestRepository crudTestRepository) {
        this.crudTestRepository = crudTestRepository;
    }
    
    @GetMapping("/")
    public String hello() {
        return "내가 왔다. 남큐킴";
    }

    @GetMapping("/next")
    public String next() {
        return "다음 단계를 가보자";
    }

    @GetMapping("/inputs/query")
    public String inputs(@RequestParam String name) {
        return "여기까지는 일단 오셨군요." + name + "님, 좋습니다.";
    }

    @GetMapping("/inputsName/{myName}")
    public String inputsName(@PathVariable String myName) {
        return "여기까지는 일단 오셨군요." + myName + "님, 좋습니다.";
    }

    public User getUser() {
        return new User("geonhee", 30);
    }

    @PostMapping("/user")
    public String createUser(@RequestBody User user) {
        return "안녕, " + user.getName() + "좀만 더 해보자" + user.getAge() + "살이잖아?";
    }

    @GetMapping("/crudTest")
    public List<CrudTest> getAllTests() {
        return crudTestRepository.findAll();
    }
}
