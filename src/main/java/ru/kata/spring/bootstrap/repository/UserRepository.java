package ru.kata.spring.bootstrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.bootstrap.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User,Integer> {

    Optional<User> findUserByName(String name);

}
