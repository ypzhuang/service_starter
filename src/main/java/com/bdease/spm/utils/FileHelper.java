/**
 * created Jan 12, 2019 by ypzhuang
 * 
 * File辅助功能
 */

package com.bdease.spm.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class FileHelper {
	public static InputStream getInputStream(String filename) throws IOException {
        Resource resource = new FileSystemResource("./" + filename);
        if(!resource.exists()) {
            resource = new ClassPathResource("/" + filename);
        }
        return resource.getInputStream();
	}
	
	public static BufferedImage getBufferedImage(URL url) throws IOException {
		return ImageIO.read(url);		
	}
	
	public static ByteArrayInputStream base64ToInputSteam(final String base64String) throws IOException {
		String rawBase64 = base64String.split(",")[1];		
		return new ByteArrayInputStream(Base64.getDecoder().decode(rawBase64.getBytes(StandardCharsets.UTF_8.name())));
	}
	
	public static String removeFileServer(String url) {
		if (url.startsWith("http") || url.startsWith("https")) {
			try {
				URL tmpURL = new URL(url);
				url = tmpURL.getPath();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	public static String removeSlash(String url) {
		if (StringUtils.isNotEmpty(url) && url.endsWith("/")) {
			return url.substring(0, url.length() - 1);
		}
		return url;
	}

	public static String addSlash(String path) {
		if (StringUtils.isNotEmpty(path) && !path.startsWith("/")) {
			return "/" + path;
		}
		return path;
	}
	
	public static void main(String args[]) {
		String url = "http://helloword/group1/hello/z.png";
		System.out.println(removeFileServer(url));
	}
}
