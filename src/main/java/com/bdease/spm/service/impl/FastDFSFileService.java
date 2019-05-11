/**
 * created Jan 12, 2019 by ypzhuang
 * 
 * FastDFS实现的文件服务
 */

package com.bdease.spm.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bdease.spm.ex.ApplicationException;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IFileService;
import com.bdease.spm.utils.FileHelper;
import com.bdease.spm.vo.FilePathResponse;
import com.github.tobato.fastdfs.domain.MetaData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Service
public class FastDFSFileService implements IFileService {	
	
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private FastFileStorageClient fastClient;

	@Value("${file.tmp-delete}")
	private boolean isTmpDelete;

	@Override
	public String uploadFile(InputStream inputStream, long fileSize, String fileExtName, Map<String, String> meta) {
		try {
			Set<MetaData> metaDataSet = new HashSet<>();
			for (Map.Entry<String, String> entry : meta.entrySet()) {
				MetaData metaData = new MetaData(entry.getKey(), entry.getValue());
				metaDataSet.add(metaData);
			}
			StorePath storePath = fastClient.uploadFile(inputStream, fileSize, fileExtName, metaDataSet);
			return storePath.getFullPath();
		} catch (Exception e) {
			log.error("上传文件失败:{}", e.getMessage());
			return null;
		}
	}
	
	@Override
	public String uploadFile(InputStream inputStream, long fileSize, String fileExtName) {
		return this.uploadFile(inputStream, fileSize, fileExtName,new HashMap<String,String>());
	}
	
	@Override
	public FilePathResponse uploadCommonFile(MultipartFile file) throws Exception, IOException {
		String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    	String filename =  FilenameUtils.getBaseName(file.getOriginalFilename());    	
    	String path = uploadFile(file.getInputStream(),file.getSize(),filename,fileExtension); 
    	return new FilePathResponse(path);
	}    
	
	@Override
	public String uploadFile(InputStream inputStream,long fileSize,String filename,String fileExtension) throws Exception {    	
    	Map<String,String> meta = new HashMap<String,String>();
    	meta.put(IFileService.FILE_NAME, filename);
    	meta.put(IFileService.EXTENSION, fileExtension);
    	meta.put(IFileService.SIZE,String.valueOf(fileSize));     
    	return this.uploadFile(inputStream, fileSize, fileExtension,meta);      
    }
    
    private String getOriginPictureType(String base64) {
    	if( base64.startsWith("data:image/jpeg;base64")) {
    		return "jpg";
    	} else if (base64.startsWith("data:image/png;base64")) {
    		return "png";
    	}     	
    	throw new ApplicationException("不支持的base64格式，当前只支持png后者jpg图片");
	}

  	 private boolean isSupportImage(String fileExtName) {
  	        return SUPPORT_IMAGE_LIST.contains(fileExtName.toUpperCase());
  	 }
  	
  	@Override
  	public FilePathResponse uploadPictureFile(MultipartFile file) throws Exception {
		String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    	Asserts.check(isSupportImage(fileExtension), "不支持的图片格式,%s" , fileExtension);    	
    	return uploadCommonFile(file);
	}
  	
  	@Override
  	public FilePathResponse uploadBase64Picture(String base64) throws Exception, FileNotFoundException, IOException {
		String fileExtension = this.getOriginPictureType(base64);
    	String fileName = JwtUser.currentUserId() < 0L? String.valueOf(JwtUser.currentUserId()) : String.valueOf(new Date().getTime());
    	try (InputStream stream = FileHelper.base64ToInputSteam(base64)) {     		
    		File file = writeToTempFile(stream, fileExtension);
    		FileInputStream inputStream = new FileInputStream(file);
    		String path = this.uploadFile(inputStream, file.length(), fileName ,fileExtension);
    		inputStream.close();
    		if(isTmpDelete) {
    			file.delete();
    		}
    		return new FilePathResponse(path);
    	}
	}
  	
	private File writeToTempFile(InputStream stream, String fileExtension) throws Exception {
		File file = File.createTempFile("fjsj", fileExtension);
		Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return file;
	}  
}
