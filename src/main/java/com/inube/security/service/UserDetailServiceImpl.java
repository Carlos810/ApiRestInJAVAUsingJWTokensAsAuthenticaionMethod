package com.inube.security.service;

import com.inube.security.model.UserModel;
import com.inube.security.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailServiceImpl  implements UserDetailsService {
    //Se usa para consultar base de datos
    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //consultamos base de datos
        UserModel userModel = this.iUserRepository.findByName(username);
        //spring security capturara esta excepcion
        if(userModel == null){
           throw new UsernameNotFoundException(username);
        }
        return new User(
                userModel.getName(),
                userModel.getPassword(),
                new ArrayList<>()
        );
    }
}
