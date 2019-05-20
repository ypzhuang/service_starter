/**
 * created Feb 10, 2019 by ypzhuang
 * 
 *省市区 功能描述
 */

package com.hptiger.starter.converter;

import java.util.List;
import lombok.Data;

@Data
public class Location {	
	String value;
	String label;
	List<Location> children;
}