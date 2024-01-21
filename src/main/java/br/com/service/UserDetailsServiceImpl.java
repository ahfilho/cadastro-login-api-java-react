package br.com.service;

import br.com.entity.User;
import br.com.repository.UserDetailsRepository;
import br.com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //    @Autowired
//    private UserDetailsRepository userDetailsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository, UserRepository userRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado para este nome: " + username));

//        Optional<User> user2 = Optional.ofNullable(userRepository.findByUserName(username).orElseThrow(() ->
//                new UsernameNotFoundException("Usuário não encontrado para este nome: " + username)));

        return new User(user.getUsername(), user.getPassword(),true, true,true,true, user.getAuthorities());


    }
}