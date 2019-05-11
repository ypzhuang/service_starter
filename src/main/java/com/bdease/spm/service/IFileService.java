/**
 * created Jan 12, 2019 by ypzhuang
 * 
 * 文件服务契约
 */

package com.bdease.spm.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import com.bdease.spm.vo.FilePathResponse;


public interface IFileService {
	
	String FILE_NAME = "filename";
	String EXTENSION = "extension";
	String SIZE = "size";
	
	String[] SUPPORT_IMAGE_TYPE = { "JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP" };
	List<String> SUPPORT_IMAGE_LIST = Arrays.asList(SUPPORT_IMAGE_TYPE);	
	
	String uploadFile(InputStream inputStream, long fileSize, String fileExtName, Map<String,String> meta);
	
	String uploadFile(InputStream inputStream, long fileSize, String fileExtName);
	
	String uploadFile(InputStream inputStream, long fileSize, String filename, String fileExtension) throws Exception;
	
	FilePathResponse uploadCommonFile(MultipartFile file) throws Exception, IOException;
	
	FilePathResponse uploadPictureFile(MultipartFile file) throws Exception;	
	
	FilePathResponse uploadBase64Picture(String base64) throws Exception, FileNotFoundException, IOException;
}
