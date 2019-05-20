/**
 * created Feb 10, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.converter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import com.hptiger.starter.utils.FileHelper;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class LocationConverter {
	private List<Location> locations;
	private static LocationConverter locationConverter;
	
	private LocationConverter() {		
	}
	
	public synchronized static  LocationConverter getInstance() {
		if(locationConverter == null) {
			locationConverter = new LocationConverter();
			locationConverter.loadData();
		}
		return locationConverter;
	}
	
	
    protected void loadData() {
		if(locations == null) {
			Gson gson = new Gson();
			String data;
			try {
				data = IOUtils.toString(FileHelper.getInputStream("location.json"),Charset.forName("UTF-8"));
				locations = gson.fromJson(data, new TypeToken<List<Location>>(){}.getType());			
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
	}
	

	public Optional<Location> getProvice(String provinceCode) {
		return locations.parallelStream().filter(entity -> entity.getValue().equals(provinceCode)).findFirst();	
	}

	
	public String getProviceName(String provinceCode) {
		return getProvice(provinceCode).map(p -> p.getLabel()).orElse(provinceCode);		
	}
	
	public  Optional<Location> getCity(String cityCode) {
		return locations.parallelStream().flatMap(p -> p.getChildren().parallelStream()).filter(c -> c.getValue().equals(cityCode)).findFirst();
		
	}
	
	public String getCityName(String cityCode) {
		return getCity(cityCode).map(p -> p.getLabel()).orElse(cityCode);		
	}

	public String getCountryName(String countryCode) {
		return getCountry(countryCode).map(p -> p.getLabel()).orElse(countryCode);		
	}
	
	public Optional<Location> getCountry(String countryCode) {
		return locations.parallelStream().flatMap(p -> p.getChildren().parallelStream()).flatMap(c->c.getChildren().parallelStream()).filter(country -> country.getValue().equals(countryCode)).findFirst();
	}
}


