package com.hptiger.starter.service.impl;

import com.hptiger.starter.service.IFileService;
import com.hptiger.starter.vo.FilePathResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class LocalFileService implements IFileService {
    @Override
    public String uploadFile(InputStream inputStream, long fileSize, String fileExtName, Map<String, String> meta) {
        return null;
    }

    @Override
    public String uploadFile(InputStream inputStream, long fileSize, String fileExtName) {
        return null;
    }

    @Override
    public String uploadFile(InputStream inputStream, long fileSize, String filename, String fileExtension) throws Exception {
        return null;
    }

    @Override
    public FilePathResponse uploadCommonFile(MultipartFile file) throws Exception, IOException {
        return null;
    }

    @Override
    public FilePathResponse uploadPictureFile(MultipartFile file) throws Exception {
        return null;
    }

    @Override
    public FilePathResponse uploadBase64Picture(String base64) throws Exception, FileNotFoundException, IOException {
        return null;
    }
}
