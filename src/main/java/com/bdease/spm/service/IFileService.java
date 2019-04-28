/**
 * created Jan 12, 2019 by ypzhuang
 * 
 * 文件服务契约
 */

package com.bdease.spm.service;

import java.io.InputStream;
import java.util.Map;

public interface IFileService {
	
	String FILE_NAME = "filename";
	String EXTENSION = "extension";
	String SIZE = "size";
	
	String uploadFile(InputStream inputStream, long fileSize, String fileExtName, Map<String,String> meta);
	
	String uploadFile(InputStream inputStream, long fileSize, String fileExtName);
}
