package com.bdease.spm.entity;

import com.bdease.spm.entity.enums.AuthorityName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Authority对象", description="")
public class Authority extends BaseEntity {	
    private static final long serialVersionUID = 3766132544824690256L;
    
	private AuthorityName name;
}
