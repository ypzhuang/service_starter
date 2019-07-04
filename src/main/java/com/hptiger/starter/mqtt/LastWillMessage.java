package com.hptiger.starter.mqtt;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LastWillMessage {
	private String command = "lastwill";
	private String deviceId;
}
