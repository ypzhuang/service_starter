/**
 * created Feb 11, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.bdease.spm.converter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.utils.FileHelper;
import com.google.gson.Gson;

import lombok.Data;


public class PicturePathConverter {
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final Random randGen = new Random();
	@Data
	class FileServerURLs {		
		
		private List<String> server;		
		private List<String> watermarkServer;
		
		public String getRandomServer() {
			return this.server.get(randGen.nextInt(this.server.size()));
		}
		
		public String getRandomWatermarkServer() {
			return this.watermarkServer.get(randGen.nextInt(this.watermarkServer.size()));
		}
	}
	private static PicturePathConverter picturePathConverter;
	private PicturePathConverter() {
		
	}
	
	private FileServerURLs fileServerURLs;
	
	public synchronized static PicturePathConverter getInstance() {
		if(picturePathConverter == null) {
			picturePathConverter = new PicturePathConverter();
			picturePathConverter.loadData();
		}
		return picturePathConverter;
	}
	
	protected void loadData() {
		Gson gson = new Gson();
		String data;
		try {
			data = IOUtils.toString(FileHelper.getInputStream("fileserver-config.json"),Charset.forName("UTF-8"));
			fileServerURLs = gson.fromJson(data, FileServerURLs.class);
			log.debug("File Server URLs:{}", fileServerURLs);
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	
    public String picturePathWithFileServer(String picturePath) {
		if(picturePath == null) return null;
		picturePath = FileHelper.removeFileServer(picturePath);		
		String fileServer;
		if(JwtUser.currentUserRoles().contains(AuthorityName.ROLE_GUEST.name()) ||
			JwtUser.currentUserRoles().contains(AuthorityName.ROLE_SHOP_USER.name()) ||
			JwtUser.currentUserRoles().contains(AuthorityName.ROLE_SHOP_ADMIN.name())
		) {
			fileServer = fileServerURLs.getRandomWatermarkServer();
		} else {
			fileServer = (fileServerURLs.getRandomServer());
		}
		return FileHelper.removeSlash(fileServer) +  FileHelper.addSlash(picturePath);
	}
    
   
}
