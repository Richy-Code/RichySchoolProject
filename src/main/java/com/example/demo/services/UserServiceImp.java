package com.example.demo.services;

import com.example.demo.entities.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.services.service_interface.UserServiceInterface;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImp implements UserServiceInterface {
    final UsersRepository usersRepository;

    public UserServiceImp(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public void saveUser(Users users) {
        users.setPassword(encryptedPassword(users.getPassword()));
        usersRepository.save(users);
    }

    @Override
    public Users getUser(String user_name) {
        return usersRepository.findByUser_name(user_name);
    }

    @Override
    public void saveAllUsers(List<Users> usersList) {
        usersList.forEach(users -> users.setPassword(encryptedPassword(users.getPassword())));
        usersRepository.saveAll(usersList);
    }

    @Override
    public List<Users> findAllUsers() {
        return (List<Users>) usersRepository.findAll();
    }

    @Override
    public Users findUserById(Long id) {
        Optional<Users> users = usersRepository.findById(id);
        return users.orElse(null);
    }

    private String encryptedPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

}
