package com.dao;



import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.model.Flow;
/**
 * 抽象类：灵活操作bean
 * @author dell
 *
 * @param <T>
 */
public abstract class templateCon<T> {
	/**
	 * 条件查询并返回bean集合
	 * @param query
	 * @param class1
	 * @return
	 */
	public abstract  List<T>  queryByCon(Query query,Class<T> t);
	
	public abstract boolean insertBeans(List<T> list,Class<T> t);
}
