/**
 * created Feb 10, 2019 by ypzhuang
 * 
 * LocationConverter unit test 功能描述
 */

package com.bdease.spm.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class LocationConverterTest {

	private LocationConverter locationSerivice = LocationConverter.getInstance();
	
	@Test
	public void getProviceName_whenInvokedWithProviceCode310000_thenGetProvinceNameShanghai() {
		//When
		String name = locationSerivice.getProviceName("310000");
		
		//Then
		assertThat(name).isEqualTo("上海市");
	}
	
	@Test
	public void getProviceName_whenInvokedWithNoneExistsProviceCode_thenGetProviceNameTheCode() {
		//When
		String name = locationSerivice.getProviceName("310001");
		
		//Then
		assertThat(name).isEqualTo("310001");
	}
	
	@Test
	public void getCityName_whenInvokedWithCityCode330400_thenGetCityNameJiaxin() {
		//When
		String name = locationSerivice.getCityName("330400");
		//Then
		assertThat(name).isEqualTo("嘉兴市");
		
	}
	
	@Test
	public void getCityName_whenInvokedWithNoneExistsCityCode_thenGetCityNameTheCode() {
		//When
		String name = locationSerivice.getCityName("330499");
		
		//Then
		assertThat(name).isEqualTo("330499");		
	}
	
	@Test
	public void getCountryName_whenInvokedWithCountryCode330402_thenGetCountryNameNanHu() {
		//When
		String name = locationSerivice.getCountryName("330402");
		
		//Then
		assertThat(name).isEqualTo("南湖区");
	}
	
	@Test
	public void getCountryName_whenInvokedWithNoneExistsCountryCode_thenGetCountryNameTheCode() {
		//When
		String name = locationSerivice.getCountryName("330999");
		
		//Then
		assertThat(name).isEqualTo("330999");
	}
}
