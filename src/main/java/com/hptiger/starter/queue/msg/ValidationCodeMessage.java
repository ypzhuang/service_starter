/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.queue.msg;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
public class ValidationCodeMessage implements Serializable {
	private static final long serialVersionUID = 4402528790644166973L;
	
	public static  String QUEUE_VALIDATION_CODE = "queue:validation_code";
	
	private String fromTenentCode;
	private String to;
	private String template;
	private Map<String,String> params;	
}
