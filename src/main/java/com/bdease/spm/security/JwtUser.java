package com.bdease.spm.security;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUser implements UserDetails {

    private static final long serialVersionUID = -7943208114673163534L;
    
    private final Long id;
    private final String username;
    private final String name;  
    private final String password;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;
    private final LocalDateTime lastPasswordResetDate; 
    private final LocalDateTime lastLoginDate;

    public JwtUser(
          Long id,
          String username,
          String name,         
          String email,
          String password, Collection<? extends GrantedAuthority> authorities,
          boolean enabled,
          LocalDateTime lastPasswordResetDate,
          LocalDateTime lastLoginDate
    ) {
        this.id = id;
        this.username = username;
        this.name = name;        
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.lastLoginDate = lastLoginDate;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

   
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public LocalDateTime getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

  
	public LocalDateTime getLastLoginDate() {
		return lastLoginDate;
	}
	
    public static JwtUser getJwtUserFromContext() {
        try {
            JwtUser user = (JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user;
        } catch(Exception e) {
            return null;
        }
    }
    
    public static Long currentUserId() {
		JwtUser user = getJwtUserFromContext();
		if (user != null) return user.getId();
		else return -2L;
    }
    
    public static String currentUserName() {
		JwtUser user = getJwtUserFromContext();
		if (user != null) return user.getName();
		else return "Anonymous";
    }	
    
    public static List<String> currentUserRoles() {
		JwtUser user = getJwtUserFromContext();
		if (user != null) return user.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toList());
		else return Collections.emptyList();
    }   
   
}
