package br.com.repository;

import br.com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.*;

public interface UserRepository  extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.cpf = ?1")
    public User userWithSameCpf(String cpf);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User userWithSameEmail(String email);

}
