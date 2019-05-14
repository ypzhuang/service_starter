/**
 * created Mar 1, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hp.tiger.starter.queue;

import java.io.Serializable;

public interface MessagePublisher {
	static  String QUEUE_VALIDATION_CODE = "queue:validation_code";

	void publish(Serializable message);
}
