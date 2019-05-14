package com.bdease.spm;


import com.bdease.spm.config.InitUserConfiguration;
import com.bdease.spm.entity.*;
import com.bdease.spm.entity.enums.*;
import com.bdease.spm.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.Arrays;


@Component
public class Bootstrap {
	private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);
	
    @PostConstruct
    public void postConstruct() {
    }

    @Autowired
    private IUserService userService;
    
    @Autowired
    private IAuthorityService authorityService;
    
    @Autowired
    private InitUserConfiguration initUserConfig;

	@PostConstruct
	public void bootstrap() {
		initUserAndRole();
	}

	private void initUserAndRole() {
		log.info("bootstrap user accounts");
		initUserConfig.getUsers().stream().forEach(u -> {
			log.info("init user: {}",u);
			Authority authority = authorityService.getOrCreateAuthorityByName(AuthorityName.getEnum(u.getRole()));		

			User user = userService.findUserByUsername(u.getUser());
			if (user == null) {
				user = new User();
				user.setUsername(u.getUser());
				user.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
				user.setName(u.getUser());
				user.setEmail(u.getUser()+"@hp.com");
				user.setEnabled(true);
				user.setLastPasswordResetDate(LocalDateTime.now());
				user.setAuthorities(Arrays.asList(authority));
				userService.saveUser(user);
			}
		});		
	}
	
	
    @Autowired
    private Environment environment;
    
	private boolean isProd() {
		return Arrays.stream(environment.getActiveProfiles()).anyMatch("prod"::equals);
	}	
}
