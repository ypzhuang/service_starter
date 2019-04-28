/**
 * created Feb 25, 2019 by ypzhuang
 * 
 * 短信发送接口
 */

package com.bdease.spm.service;

import java.util.List;

public interface ISMSService {	
    boolean sendMessage(String phoneNumbers,String template,Object model);
    boolean sendMessage(List<String> phoneNumbers,String template,Object model);
    boolean validateCode(String phone, String code);
}