package ru.kata.spring.boot_security.demo.entites;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Size(min = 3, max = 100, message = "Names length should be from 2 to 100 characters")
    @Column(name = "name")
    private String name;

    @Size(min = 3, max = 100, message = "Password length should be from 2 to 100 characters")
    @Column(name = "password")
    private String password;

    @Size(min = 3, max = 100, message = "Surnames length should be from 2 to 100 characters")
    @Column(name = "surname")
    private String surname;

    @Min(value = 1900, message = "Year of birth should be more than 1900")
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @NotEmpty(message = "Choose at least one role")
    public Set<Role> getRoles() {
        return roles;
    }

    public User() {
    }

    public User(int id, String name, int yearOfBirth, String password) {
        this.id = id;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", password='" + password + '\'' +
                '}';
    }
}
