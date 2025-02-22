package ru.kata.spring.bootstrap.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


//    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

//    @Size(min = 4, max = 100, message = "Password length should be from 4 to 100 characters")
    @Column(name = "password")
    private String password;

//    @Size(min = 3, max = 100, message = "First names length should be from 3 to 100 characters")
    @Column(name = "first_name")
    private String firstName;

//    @Size(min = 3, max = 100, message = "Last name length should be from 3 to 100 characters")
    @Column(name = "last_name")
    private String lastName;

//    @Min(value = 14, message = "Age should at least 14")
    @Column(name = "age")
    private int age;

    @ManyToMany(fetch = FetchType.EAGER)
//    @NotEmpty(message = "Choose at least one role")
    private Set<Role> roles;

    public User() {
    }

    public User(int id, String email, String password, String firstName, String lastName, int age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
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
}
