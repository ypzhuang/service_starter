/**
 * created Feb 12, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PicturePathConverterTest {

	@Test
	public void picturePathWithFileServer_whenInvokedWithRelativePicturePath_thenGetThePictureURL() {
		
		//When
		 String pictureURL = PicturePathConverter.getInstance().picturePathWithFileServer("group/zz/hello.png");
		
		//Then
		assertThat(pictureURL).isIn("http://47.96.166.81:8888/group/zz/hello.png","http://116.62.71.222:8888/group/zz/hello.png");
	}
	
	@Test
	public void picturePathWithFileServer_whenInvokedWithAbsolutePicturePath_thenGetThePictureURL() {
		
		//When
		 String pictureURL = PicturePathConverter.getInstance().picturePathWithFileServer("/group/zz/hello.png");
		
		//Then
		assertThat(pictureURL).isIn("http://47.96.166.81:8888/group/zz/hello.png","http://116.62.71.222:8888/group/zz/hello.png");
	}
	
	@Test
	public void picturePathWithFileServer_whenInvokedWithAnyPictureURL_thenGetTheNewPictureURL() {		
		//When
		 String pictureURL = PicturePathConverter.getInstance().picturePathWithFileServer("http://baidu.com/group/zz/hello.png");
		
		//Then
		assertThat(pictureURL).isIn("http://47.96.166.81:8888/group/zz/hello.png","http://116.62.71.222:8888/group/zz/hello.png");
	}
}
