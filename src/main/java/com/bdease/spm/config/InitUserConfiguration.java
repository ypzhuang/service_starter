/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.bdease.spm.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "role")
@Configuration
@Data
@NoArgsConstructor
public class InitUserConfiguration {
	List<InitUser> users;
	
	@Data
	@NoArgsConstructor
	public static class InitUser {		 
		  private String role;
	      private String user;
	      private String password;
	}
}
