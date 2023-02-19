package com.example.moneymanager2.service;

import com.example.moneymanager2.model.User;
import com.example.moneymanager2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean save(User user){
        try{
            userRepository.save(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(User user){
        try{
            userRepository.delete(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public User findById(String id){
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    public boolean Update(User user){
        try{
            userRepository.save(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<User> findAll(){
        try{
            List<User> users = userRepository.findAll();
            return users;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
