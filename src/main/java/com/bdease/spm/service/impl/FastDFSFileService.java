/**
 * created Jan 12, 2019 by ypzhuang
 * 
 * FastDFS实现的文件服务
 */

package com.bdease.spm.service.impl;

import java.io.InputStream;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.bdease.spm.service.IFileService;
import com.github.tobato.fastdfs.domain.MetaData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Service
public class FastDFSFileService implements IFileService {
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	
	 @Autowired
	 private FastFileStorageClient fastClient;

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
}
