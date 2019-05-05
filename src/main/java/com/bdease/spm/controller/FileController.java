/**
 * created Jan 12, 2019 by ypzhuang
 * 
 * 上传文件或者Base64图片
 */

package com.bdease.spm.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bdease.spm.ex.ApplicationException;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IFileService;
import com.bdease.spm.utils.FileHelper;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api/v1/files")
@Api(tags = {"File"})
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER','ROLE_SHOP_ADMIN','ROLE_SHOP_USER')")
public class FileController extends BaseController {
	private static final String[] SUPPORT_IMAGE_TYPE = { "JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP" };
	private static final List<String> SUPPORT_IMAGE_LIST = Arrays.asList(SUPPORT_IMAGE_TYPE);	
	 
	@Autowired
	private IFileService fileService;
	
	
	@Value("${file.tmp-delete}")
	private boolean isTmpDelete;
	
	
    @PostMapping
    @ApiOperation(value = "通用上传文件")
	public FilePathResponse upload(@RequestParam(required = true) MultipartFile file) throws Exception {
    	String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    	String filename =  FilenameUtils.getBaseName(file.getOriginalFilename());    	
    	String path = uploadFile(file.getInputStream(),file.getSize(),filename,fileExtension); 
    	return new FilePathResponse(path);
	}    
    
    @PostMapping("/picture")
    @ApiOperation(value = "上传图片,支持的文件格式：\"JPG\", \"JPEG\", \"PNG\", \"GIF\", \"BMP\", \"WBMP\"")
	public FilePathResponse uploadPicture(@RequestParam(required = true) MultipartFile file) throws Exception {
    	String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    	Asserts.check(isSupportImage(fileExtension), "不支持的图片格式,%s" , fileExtension);    	
    	return upload(file);
	}
    
    @PostMapping("/base64")
    @ApiOperation(value = "上传图片,base64格式，支持的原始文件格式：\"JPG\", \"JPEG\", \"PNG\"")
	public FilePathResponse uploadBase64Picture(@RequestBody(required = true) String base64) throws Exception {
    	String fileExtension = getOriginPictureType(base64);
    	String fileName = JwtUser.currentUserId() < 0L? String.valueOf(JwtUser.currentUserId()) : String.valueOf(new Date().getTime());
    	try (InputStream stream = FileHelper.base64ToInputSteam(base64)) {     		
    		File file = writeToTempFile(stream, fileExtension);
    		FileInputStream inputStream = new FileInputStream(file);
    		String path = uploadFile(inputStream, file.length(), fileName ,fileExtension);
    		inputStream.close();
    		if(isTmpDelete) {
    			file.delete();
    		}
    		return new FilePathResponse(path);
    	}
    }
    
    private File writeToTempFile(InputStream stream,String fileExtension) throws Exception {    	
    	File file = File.createTempFile("fjsj",fileExtension);
		Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING); 
		return file;
    }
    private String uploadFile(InputStream inputStream,Long fileSize,String filename,String fileExtension) throws Exception {    	
    	Map<String,String> meta = new HashMap<String,String>();
    	meta.put(IFileService.FILE_NAME, filename);
    	meta.put(IFileService.EXTENSION, fileExtension);
    	meta.put(IFileService.SIZE,String.valueOf(fileSize));     
    	return fileService.uploadFile(inputStream, fileSize, fileExtension,meta);      
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
}

class FilePathResponse {
	private String path;

	public FilePathResponse(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	
}
