package com.hptiger.starter.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hptiger.starter.entity.enums.DelFlag;
import com.hptiger.starter.entity.enums.MessageClass;
import com.hptiger.starter.entity.enums.MessageStatus;
import com.hptiger.starter.entity.enums.MessageType;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="AppMessage对象", description="消息")
public class AppMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag;

    @ApiModelProperty(value = "消息ID，对外")
    private String messageId;

    @ApiModelProperty(value = "App ID")
    private String appId;

    @ApiModelProperty(value = "MQ Message ID")
    private String mqMsgId;

    @ApiModelProperty(value = "消息到达接口时间")
    private LocalDateTime receiveDate;

    @ApiModelProperty(value = "消息发布到MQ时间")
    private LocalDateTime queuingDate;

    @ApiModelProperty(value = "消息请求发送时间")
    private LocalDateTime sendDate;

    @ApiModelProperty(value = "消息取消时间")
    private LocalDateTime cancelDate;

    @ApiModelProperty(value = "状态")
    private MessageStatus status;

    @ApiModelProperty(value = "缘由")
    private String reason;

    @ApiModelProperty(value = "消息大类")
    private MessageClass messageClass;

    @ApiModelProperty(value = "消息小类")
    private MessageType messageType;

    @ApiModelProperty(value = "消息内容")
    private String content;
}
