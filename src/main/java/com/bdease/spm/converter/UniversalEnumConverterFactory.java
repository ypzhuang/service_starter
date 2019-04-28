/**
 * created Jan 6, 2019 by ypzhuang
 * 
 * 枚举类型处理
 */

package com.bdease.spm.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.baomidou.mybatisplus.core.enums.IEnum;

@SuppressWarnings("rawtypes")
public class UniversalEnumConverterFactory implements ConverterFactory<String, IEnum> {
	private static final Map<Class, Converter> converterMap = new WeakHashMap<>();
	
	@Override
	public <T extends IEnum> Converter<String, T> getConverter(Class<T> targetType) {
		@SuppressWarnings("unchecked")
		Converter<String, T> result = converterMap.get(targetType);
        if(result == null) {
            result = new IntegerStrToEnum<T>(targetType);
            converterMap.put(targetType, result);
        }
        return result;
	}
	
	class IntegerStrToEnum<T extends IEnum> implements Converter<String, T> {
        private Map<String, T> enumMap = new HashMap<>(); 
        public IntegerStrToEnum(Class<T> enumType) {           
            T[] enums = enumType.getEnumConstants();
            for(T e : enums) {
                enumMap.put(e.getValue().toString(), e);
            }
        } 
 
        @Override
        public T convert(String source) {
            T result = enumMap.get(source);
            if(result == null) {
                throw new IllegalArgumentException("No element matches " + source);
            }
            return result;
        }
    }
}
