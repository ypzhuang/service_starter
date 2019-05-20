/**
 * created Jan 3, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.vo;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictTypeVO {
	@ApiModelProperty(value = "所属模块")
	private String module;

	@ApiModelProperty(value = "类别名称")
	private String typeName;

	@ApiModelProperty(value = "类别代码")
	private String typeCode;
	
	private List<DictCodeVO> codes = new ArrayList<DictCodeVO>();
	
	public void addDictCode(DictCodeVO dictCode)  {
		this.getCodes().add(dictCode);
	}
	
}
