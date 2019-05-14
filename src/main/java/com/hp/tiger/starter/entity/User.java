package com.hp.tiger.starter.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.hp.tiger.starter.entity.enums.AuthorityName;
import com.hp.tiger.starter.entity.enums.DelFlag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
@ApiModel(value="User对象", description="")
@ToString(callSuper=true, includeFieldNames=true)
public class User extends BaseEntity {
	private static final long serialVersionUID = -6680466718330997381L;

	@ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag;
	
	private String email;

    private Boolean enabled;

    private LocalDateTime lastLoginDate;

    private String lastLoginIp;

    private LocalDateTime lastPasswordResetDate;

    private String name;

    private String password;

    @ApiModelProperty(value = "手机号/用户名")
    private String username;

    @ApiModelProperty(value = "入职日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate onboardDate;

    @ApiModelProperty(value = "离职日志")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastDate;
    
    @JsonIgnore
    private transient List<Authority> authorities; 

    private transient List<AuthorityName> authorityNames;

}
