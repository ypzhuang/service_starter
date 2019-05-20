package com.hptiger.starter.vo;


import com.hptiger.starter.entity.enums.EmailContentType;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Message
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-16
 */
@Data
@ApiModel(value="EmailMessageVO", description="邮件消息")
public class EmailMessageVO {
	@ApiModelProperty(value = "邮件接受者", required=true)
	private List<@NotNull(message="邮件接受者不能为空") @Email(message="邮件接受者:非邮件地址") String> to;

	@ApiModelProperty(value = "邮件抄送者")
	private List<@Email(message="邮件抄送者:非邮件地址") String> cc;
	
	@ApiModelProperty(value = "邮件秘抄者")
	private List<@Email(message="邮件秘抄者:非邮件地址") String> bcc;
	
	@ApiModelProperty(value = "回复邮件地址")
	private List<@Email(message="回复邮件地址:非邮件地址") String> replyTo;
	
	@ApiModelProperty(value = "邮件主题", required=true)
	@NotNull(message="邮件主题不能为空")
	private String subject;
	
	@ApiModelProperty(value = "邮件正文类型")
	private EmailContentType contentType = EmailContentType.PLAIN;
	
	@ApiModelProperty(value = "邮件正文", required=true)
	@NotNull(message="邮件正文不能为空")
	private String content;
	
	@ApiModelProperty(value = "附件URL列表")
	private List<String> attachments;
}
