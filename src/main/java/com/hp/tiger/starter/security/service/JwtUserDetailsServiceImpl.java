package com.hp.tiger.starter.security.service;

import com.hp.tiger.starter.security.JwtUserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hp.tiger.starter.entity.User;
import com.hp.tiger.starter.service.IUserService;


@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
	protected  final Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {    	
        User user = userService.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("用户名'%s'不存在。", username));
        } else {
        	if (!user.getEnabled()) throw new DisabledException(String.format("用户名'%s'的账户已停用。", username));
            return JwtUserFactory.create(user);
        }
    }    
}
