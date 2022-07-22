package com.example.demo.repository;

import com.example.demo.entities.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends CrudRepository<Users, Long> {
    @Query("SELECT u FROM Users u WHERE u.user_name = :user_name")
    Users findByUser_name(@Param("user_name") String user_name);
}
