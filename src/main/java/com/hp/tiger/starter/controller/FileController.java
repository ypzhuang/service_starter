/**
 * created Jan 12, 2019 by ypzhuang
 * 
 * 上传文件或者Base64图片
 */

package com.hp.tiger.starter.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hp.tiger.starter.service.IFileService;
import com.hp.tiger.starter.vo.FilePathResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api/v1/files")
@Api(tags = {"File"})
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER')")
public class FileController extends BaseController {
	 
	@Autowired
	private IFileService fileService;		
	
    @PostMapping
    @ApiOperation(value = "通用上传文件")
	public FilePathResponse upload(@RequestParam(required = true) MultipartFile file) throws Exception {
    	return this.fileService.uploadCommonFile(file);
	}	  
    
    @PostMapping("/picture")
    @ApiOperation(value = "上传图片,支持的文件格式：\"JPG\", \"JPEG\", \"PNG\", \"GIF\", \"BMP\", \"WBMP\"")
	public FilePathResponse uploadPicture(@RequestParam(required = true) MultipartFile file) throws Exception {
    	return this.fileService.uploadPictureFile(file);
	}	
    
    @PostMapping("/base64")
    @ApiOperation(value = "上传图片,base64格式，支持的原始文件格式：\"JPG\", \"JPEG\", \"PNG\"")
	public FilePathResponse uploadBase64Picture(@RequestBody(required = true) String base64) throws Exception {
    	return this.fileService.uploadBase64Picture(base64);
    }    
}

