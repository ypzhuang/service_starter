/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.bdease.spm.queue;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
public class ValidationCodeMessage implements Serializable {
	private static final long serialVersionUID = 4402528790644166973L;
	
	private String fromTenentCode;
	private String to;
	private String template;
	private Map<String,String> params;	
}
