package com.service;

import java.util.List;
import java.util.Map;

import com.model.Cars;
import com.model.Flow;
import com.mongodb.MongoClient;

public interface SaveAndqueryService{
	/**
	 * 保存json字符串
	 * @param flowStr
	 * 指定集合documentName
	 */
	 public boolean saveJsonStr(MongoClient mongoClient, String flowStr);
	 
	 
	 /***
	  * 将list<String>存到MongoDB
	  * @param str
	  * @return
	  */
	 public boolean saveJsonListStr(MongoClient mongoClient,List<String> jsonStr);
	 /**
		 * 保存json字符串
		 * @param flowStr
		 * 指定实体Flow
		 */
	 public void insertBeanStr(String jsonStr);
	 /***
	  * 根据条件查询bean
	  * @param str
	  * @return
	  */
	 public List<Flow> queryBeanInfoBycondition(Map<String, Object> map1);
	 /**
	  * 条件查询
	  * type是否分页
	  * isRegex:查询方式精确or模糊
	  * flow：实体数据
	  * pageNo：当前页
	  * pageSize：每页记录数量
	  * @param data
	  * @return
	  */
	 public String queryPageBycondition(Map<String, Object> map);
	 
	 /**
	  * 查询集合，返回数据list
	  * @param site
	  * @return
	  */
	 public List<String> queryAllList(String site);
	 
	 public void updateOrSave(String str);
	 
	 /**
	  * 验证附件在表中是否已经存在
	  */
	 public String checkMD5Info(String id);
	 
	 public String queryPrevious(Map<String, Object> map);
	 
	 public String queryNext(Map<String, Object> map);
}
 