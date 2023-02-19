package com.example.moneymanager2.controller;

import com.example.moneymanager2.model.Book;
import com.example.moneymanager2.model.User;
import com.example.moneymanager2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public boolean save(@RequestBody User user){
        return userService.save(user);
    }

    @GetMapping
    public List<User> getAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") String id){
        return  userService.findById(id);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable("id") String id, @RequestBody User user){
        User userEx = findById(id);
        userEx.setName(user.getName());
        userEx.setEmail(user.getEmail());
        userEx.setUrlPic(user.getUrlPic());
        return userService.Update(userEx);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id){
        User userEx = findById(id);
        return userService.delete(userEx);
    }
}
