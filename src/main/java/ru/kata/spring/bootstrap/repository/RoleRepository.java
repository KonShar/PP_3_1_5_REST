package ru.kata.spring.bootstrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.bootstrap.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

}
