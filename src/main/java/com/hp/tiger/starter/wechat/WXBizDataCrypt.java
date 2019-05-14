/**
 * created Feb 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hp.tiger.starter.wechat;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

public class WXBizDataCrypt {
	
	private static final String WATERMARK = "watermark";
	private static final String APPID = "appid";
	private static final String OPENID = "openId";
	
	private String appId; 	
	
	public WXBizDataCrypt(String appId) {
		this.appId = appId;
	}

	public  String decrypt(String encryptedData, String sessionKey, String iv){
		String result = "";
		try {
			AES aes = new AES();  
		    byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));  
		    if(null != resultByte && resultByte.length > 0){  
		        result = new String(WxPKCS7Encoder.decode(resultByte)); 		     
		    	JSONObject jsonObject = new JSONObject(result);
		    	String openId = jsonObject.getString(OPENID);
		    	System.out.println(openId);
		    	String decryptAppid = jsonObject.getJSONObject(WATERMARK).getString(APPID);
		    	if(!this.appId.equals(decryptAppid)){
		    		result = "";
		    	}
	        }  
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
	    return result;
	}
	
	
	public static void main(String[] args) throws Exception{
	   String appId = "wx209e977d979fe4cf";
	   String encryptedData = "W8WW+qJaKWdOm/WtWGen2O5STBAhGK/+Ilit2bebSXYf1RVQBY3Y5PDbByLGsqd5BXPXARJvaZuP/7DVDSyFybLzjXBK5Upkg5R8tqYcKTZsoGx0FC95oBvCh7bzZ9OGe1OUwU2Cu8XsOa2rrgw6FW5DNWZP5nnfO3J3BTDROw3yN8lKhaH91Plot8cmVK8LLJkaPqk4Wp85NvnWZjvYtnjfEGJNtEogfAbTBSb1nlefBC1fINBy4sxvn2UdpIl+cNseIMa/WjGR3JHNepMnTdyHlRXHpicYa/LIKHMxw9U1714IhmJByRCnrmTyu8k6JnL1AHrzjT/BgxA4AhtdsKNmXHD83An8MpBRQTrUa/KJ0thcUQHKvir5s3wXJ3m2j2hgPgemyKtVo2hnL5n4tij+O4/sqlVbOOUdiyBcjdbvDfHP4ubJ/fYSoE3v3QOqRaPfYOpSkO42RZsfaF3fIcPS4KRjHQm2o7XyIN4USHE=";
	   String sessionKey = "1vYno9sPJoPlpcbKvhu9Yw==";
	   String iv = "cZa9eaahymS3xba9wO7aTQ==";
	   
	   WXBizDataCrypt core = new WXBizDataCrypt(appId);
       System.out.println(core.decrypt(encryptedData, sessionKey, iv));
    }
}
