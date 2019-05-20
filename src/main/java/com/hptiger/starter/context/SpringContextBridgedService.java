/**
 * created Feb 25, 2019 by ypzhuang
 * 
 * SpringContextBridgedService 功能描述
 */

package com.hptiger.starter.context;

import java.util.function.Supplier;


public interface SpringContextBridgedService {
	<T> T getService(Class<T> serviceType);

	<T> T getService(String name, Class<T> serviceType);

	<T> void registerBean(String beanName, Class<T> serviceType, Supplier<T> supplier);

	<T> void registerBean(Class<T> serviceType, Supplier<T> supplier);
}
