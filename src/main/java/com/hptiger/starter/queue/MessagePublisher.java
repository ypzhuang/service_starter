/**
 * created Mar 1, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.queue;

import java.io.Serializable;

public interface MessagePublisher {
	void publish(Serializable message);
}
