package com.bdease.spm.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bdease.spm.entity.enums.DelFlag;
import com.bdease.spm.entity.enums.GuestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="MiniProgramUser对象", description="")
public class MiniProgramUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag;

    private String nickName;

    private Integer gender;

    private String language;

    private String city;

    private String province;

    private String country;

    private String avatarUrl;

    private String unionId;

    private String openId;

    @ApiModelProperty(value = "注册日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime joinTime;

    private String column1;

    private String column2;

    private String phone;

    private Long shopId;

    private String shopName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastOrderDate;
    
    @ApiModelProperty(value = "订单数")
    private Long orderCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastPictureDate;

    private String name;

    private GuestStatus status = GuestStatus.NORMAL;
}
