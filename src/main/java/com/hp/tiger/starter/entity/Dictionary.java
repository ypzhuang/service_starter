package com.hp.tiger.starter.entity;

import com.hp.tiger.starter.entity.enums.Enable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author John Zhuang
 * @since 2019-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Dictionary对象", description="")
public class Dictionary extends BaseEntity {
	
    private static final long serialVersionUID = 2053084958977477822L;

	@ApiModelProperty(value = "是否启用")
    private Enable enable;

    @ApiModelProperty(value = "所属模块")
    private String module;

    @ApiModelProperty(value = "类别名称")
    private String typeName;

    @ApiModelProperty(value = "类别代码")
    private String typeCode;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "备注")
    private String remark;

}
