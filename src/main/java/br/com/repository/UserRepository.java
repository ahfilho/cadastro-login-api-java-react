package br.com.repository;

import br.com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.cpf = ?1")
    public User userWithSameCpf(String cpf);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User userWithSameEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    String findByName();

    @Query("SELECT c FROM User c WHERE c.cpf = ?1")
    public User clientWithSameCpf(String cpf);

    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    User findByNome(String nome);

    UserDetails findByUserName(String userName);
//
//    @Query("SELECT u FROM User u WHERE u.cpf = :cpf AND u.password = :password")
//    User findByUsernameAndPassword(@Param("cpf") String cpf, @Param("password") String password);

//    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")

//    public User findByUsernameIgnoreCase(@Param("username") String username);


//    @Query("SELECT u FROM User u WHERE LOWER(u.userName) = LOWER(:username)")
//    Optional<User> findByUseName(String username);
}
