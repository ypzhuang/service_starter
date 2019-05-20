package com.hptiger.starter.entity;

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
@ApiModel(value="UserAuthority对象", description="")
public class UserAuthority extends BaseEntity {

    private static final long serialVersionUID = -3376695370328748594L;

	private Long userId;

    private Long authorityId;

}
