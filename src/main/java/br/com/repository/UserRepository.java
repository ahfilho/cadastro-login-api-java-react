package br.com.repository;

import br.com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.*;

public interface UserRepository  extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.cpf = ?1")
    public User userWithSameCpf(String cpf);

    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    public User userWithSameEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    String findByName();

    @Query("SELECT c FROM User c WHERE c.cpf = ?1")
    public User clientWithSameCpf(String cpf);

    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    User findByNome(String nome);}
