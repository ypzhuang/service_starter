/**
 * created Jan 3, 2019 by ypzhuang
 * 
 * LambdaQueryWrapper 适配器
 */

package com.hptiger.starter.adapter;


import java.util.Collection;
import java.util.List;

import com.hptiger.starter.utils.NumberHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;


public class LambdaQueryWrapperAdapter<T> extends LambdaQueryWrapper<T> {
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = 1L;

	public LambdaQueryWrapperAdapter() {
		super();
	}

	public LambdaQueryWrapperAdapter<T> eq(SFunction<T, ?> column, Object value) {
		super.eq(value != null && !StringUtils.isAnyBlank(value.toString()), column, value);
		return this;
	}
	
	public LambdaQueryWrapperAdapter<T> eq(SFunction<T, ?> column, List<? extends Object> values) {
		if (CollectionUtils.isNotEmpty(values)) {
			super.nested(i -> {
				int k = 0;
				for (Object value : values) {
					i.eq(value != null && !StringUtils.isAnyBlank(value.toString()), column, value);
					if (k != values.size() - 1) {
						i.or();
					}
					k++;
				}
				return i;
			});
		}
		return this;		
	}

	public LambdaQueryWrapperAdapter<T> like(SFunction<T, ?> column, Object value) {
		super.like(value != null && !StringUtils.isAnyBlank(value.toString()), column, value);
		return this;
	}

	@SuppressWarnings("unchecked")
	public LambdaQueryWrapperAdapter<T> groupBy(SFunction<T, ?>... columns) {
		super.groupBy(columns);
		return this;
	}

	public LambdaQueryWrapperAdapter<T> scope(SFunction<T, ?> column, Scope value) {		
		if (value != null) {
			super.ge(value.getLow() != null, column, value.getLow());
			super.le(value.getHigh() != null, column, value.getHigh());
		}
		return this;
	}
	
	public LambdaQueryWrapperAdapter<T> scope(SFunction<T, ?> column, List<? extends Scope> values) {
		// TODO 优化，取并集
		if (CollectionUtils.isNotEmpty(values)) {
			super.nested(i -> {
				int k = 0;
				for (Scope value : values) {
					i.ge(value.getLow() != null, column, value.getLow()).le(value.getHigh() != null, column, value.getHigh());
					if (k != values.size() - 1) {
						i.or();
					}
					k++;
				}
				return i;
			});
		}
		return this;
	}	

	public LambdaQueryWrapperAdapter<T> le(SFunction<T, ?> column, Object value) {
		super.le(value != null && !StringUtils.isAnyBlank(value.toString()), column, value);
		return this;
	}

	public LambdaQueryWrapperAdapter<T> isNullOrLe(SFunction<T, ?> column, Object value) {
		if(value != null && !StringUtils.isAnyBlank(value.toString())) {
			super.nested(i -> {
				return i.isNull(column).or().le(column, value);
			});
		}
		return this;
	}


	
	public LambdaQueryWrapperAdapter<T> ge(SFunction<T, ?> column, Object value) {
		super.ge(value != null && !StringUtils.isAnyBlank(value.toString()), column, value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public LambdaQueryWrapperAdapter<T> nestedLike(Object value, SFunction<T, ?> ...columns) {
		// TODO 此处需要优化
		if(value != null && !StringUtils.isAnyBlank(value.toString())) {
			super.nested(i -> {
				int k = 0;
				for (SFunction<T, ?> column: columns) {
					i.like(column, value);
					if (k != columns.length - 1) {
						i.or();
					}
					k++;
				}
				return i;
			});
		}
        return this;
    }
	
	public LambdaQueryWrapperAdapter<T> nestedLike(SFunction<T, ?> column, List<? extends IEnum<String>> values) {		
		if(CollectionUtils.isNotEmpty(values)) {
			super.nested(i -> {
				int k = 0;
				for (IEnum<String> value: values) {
					i.like(column, value.getValue());
					if (k != values.size() - 1) {
						i.or();
					}
					k++;
				}
				return i;
			});
		}
        return this;
    }	

	public LambdaQueryWrapperAdapter<T> nestedScopes(SFunction<T, ?> lowColumn, SFunction<T, ?> highColumn, List<? extends Scope> values) {		
		log.debug("nestedScope, Scope values:{}",values);
		if(CollectionUtils.isNotEmpty(values)) {
			super.nested(i -> {
				int k = 0;
				for(final Scope v: values) {
					final int low = NumberHelper.ifNullToMIN(v.getLow());
					final int high = NumberHelper.ifNullToMAX(v.getHigh());
					i.nested(j -> j.ge(lowColumn, low).le(lowColumn, high)
							.or()
							.le(lowColumn, low).ge(highColumn,high)
							.or()
							.le( lowColumn, low).ge( highColumn, high)
							.or()
							.ge(lowColumn, low).le( highColumn, high)
					);
					if(k != values.size() - 1) i.or();
					k++;
				}				
				return i;
			});
		}
		return this;
	}

	public LambdaQueryWrapperAdapter<T> in(SFunction<T, ?> column, Collection<?> value) {
		super.in(value != null && !value.isEmpty(), column,value);
		return this;
	}
}
