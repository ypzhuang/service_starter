/**
 * created Jan 3, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hp.tiger.starter.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.hp.tiger.starter.entity.enums.Enable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictCodeVO {
	
	private Long id;

	@ApiModelProperty(value = "值")
	private String value;

	@ApiModelProperty(value = "名称")
	@NotNull(message = "名称不能为空")
	@Size(min = 2, max=20,message = "名称至少2个字符,最多20个字符")
	private String name;
	
	@ApiModelProperty(value = "备注")
	private String remark;
	
	@ApiModelProperty(value = "是否启用")
	@NotNull(message = "是否启用不能为空")
	private Enable enable;

}
