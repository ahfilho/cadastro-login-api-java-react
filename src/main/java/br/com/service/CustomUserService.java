package br.com.service;

import br.com.entity.User;
import br.com.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service
public class CustomUserService implements UserDetailsService {


    private final UserDetailsRepository userDetailsRepository;

    public CustomUserService(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDetailsRepository.findByUserName(userName);
        if (null == user) {
            throw new UsernameNotFoundException("Nome de usuário não encontrado:" + userName);
        }
        return user;
    }
}
