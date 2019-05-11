package com.bdease.spm.controller.app.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.service.IFileService;
import com.bdease.spm.vo.FilePathResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/app/emp/v1/files")
@Api(tags={"MiniEmp"})
@PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
public class MiniFileController extends MiniBaseController {
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
