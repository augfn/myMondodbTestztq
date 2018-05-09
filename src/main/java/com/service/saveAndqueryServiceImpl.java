package com.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.dao.queryModelInfo;
import com.google.gson.Gson;
import com.model.Flow;
import com.model.MD5Value;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

@Service("saveAndqueryService")
public class saveAndqueryServiceImpl implements SaveAndqueryService{
	@Autowired
    private MongoTemplate mongoTemplate;
	
	queryModelInfo info = new queryModelInfo();
	
	public boolean saveJsonStr(MongoClient mongoClient,String jsonStr) {
		@SuppressWarnings("unchecked")
		/*String str[] = jsonStr.split("@&");
		List<String> list = Arrays.asList(str);*/
		DBCollection newsCollection =null;
		String mapKey=null;
		DBObject bson =null;
		List<DBObject> bsonMap = new ArrayList<DBObject>();
//		for (String string : list) {
			Map<String,Object> jsonMap = (Map<String,Object>) JSON.parse(jsonStr);
			Set<Entry<String, Object>> set = jsonMap.entrySet();
			for (Entry<String, Object> entry  :set) {
				mapKey = entry.getKey();
				if(!StringUtils.isEmpty(entry.getValue())){
					bson =(DBObject)entry.getValue();
				}
				if(newsCollection==null){
					newsCollection  = mongoClient.getDB("hist").getCollection(mapKey);
				}
				bsonMap.add(bson);
			}
//		}
		WriteResult result = newsCollection.insert(bsonMap);
		System.out.println(jsonStr);
		if (null != result) {
			System.out.println(result.getN());
            if (result.getN() > 0) {
                return true;
            }
        }
		
		return false;
	}
	/** 
	* @功能: 将list<String>存到MongoDB
	* @return boolean    返回类型
	* @author :ZTQ 
	* @date 2018年5月8日 上午10:18:22  
	*/
	public boolean saveJsonListStr(MongoClient mongoClient,List<String> jsonStr) {
		System.out.println("222222222222222222222222");
		if(jsonStr==null){
		      return (Boolean) null;
		   }
		StringBuilder results = new StringBuilder();
		boolean first = true;
		for(String string :jsonStr) {
			if(first) {
				first=false;
			}else{
				results.append(",");
			}
			results.append(string);
		}
		String ss = results.toString();
		Map<String,Object> jsonMap = (Map<String,Object>) JSON.parse(ss);
		Set<Entry<String, Object>> set = jsonMap.entrySet();
		DBCollection newsCollection =null;
		String mapKey=null;
		DBObject bson =null;
		List<DBObject> bsonMap = new ArrayList<DBObject>();
		for (Entry<String, Object> entry  :set) {
			mapKey = entry.getKey();
			if(!StringUtils.isEmpty(entry.getValue())){
				bson =(DBObject)entry.getValue();
			}
			if(newsCollection==null){
				 newsCollection  = mongoClient.getDB("hist").getCollection(mapKey);
			}
			bsonMap.add(bson);
		}
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+bsonMap.size());
		WriteResult result = newsCollection.insert(bsonMap);
		System.out.println(jsonStr);
		if (null != result) {
			System.out.println(result.getN());
            if (result.getN() > 0) {
                return true;
            }
        }
		
		return false;
	}
	
	public void insertBeanStr(String jsonStr) {
		List<Flow> list = new ArrayList<Flow>();
    	Gson gson = new Gson();
    	Flow flow = gson.fromJson(jsonStr,Flow.class);
    	list.add(flow);
    	info.insertBeans(list, Flow.class);
	}
	public String  queryPageBycondition(Map<String, Object> map1) {
		String result=null;
		BasicDBObject searchQuery = new BasicDBObject();
		BasicDBObject order = new BasicDBObject();
		BasicDBList condList = new BasicDBList();//存放查询条件的集合  
		BasicDBObject searchQuery_or = new BasicDBObject();
		BasicDBObject searchQuery_or1 = new BasicDBObject();
		BasicDBObject searchQuery_or2 = new BasicDBObject();
		BasicDBObject searchQuery_or3 = new BasicDBObject();
		BasicDBList condList_or = new BasicDBList();//or
		Pattern pattern;//用在模糊查询得时候，对字段进行匹配 
		String mapKey = null;
		String mapKey1 = null;
		BasicDBObject condition = null;
		Set<Entry<String, Object>> set1 = map1.entrySet(); 
		for (Entry<String, Object> entry : set1) {
			String k = entry.getKey();
			if(!"isExists".equals(k) &&!"loadingMan".equals(k) &&!"oderType".equals(k) &&!"oderField".equals(k) &&!"type".equals(k)&& !"isRegex".equals(k)&& !"oderType".equals(k)&& !"oderField".equals(k)&& !"pageNo".equals(k)&& !"pageSize".equals(k)&& !"".equals(k) && !StringUtils.isEmpty(k)){
				mapKey1 = k;
			}				
		}
		String type = map1.get("type")+"";//是否分页
		String isRegex = map1.get("isRegex")+"";//是否模糊查询
		int pageNo = Integer.parseInt(map1.get("pageNo")+"");//当前页
		int pageSize = Integer.parseInt(map1.get("pageSize")+"");//每页数量
		String oderField = map1.get("oderField")+"";//排序字段
		String oderType = map1.get("oderType")+"";//排序字段
		if("DESC".equals(oderType)){//降序 -1
			oderType = "-1";
		}else if("ASC".equals(oderType)){
			oderType = "1";
		}
		String loading=null;
		String[] loadingMan = null;
		String loginMan=null;
		String loading1=null;
		String loading2=null;
		String loading3=null;
		String loading4=null;
		String loading5=null;
		String loading6=null;
		String loading7=null;
		if(map1.get("loadingMan")!=null){
			 loading = map1.get("loadingMan")+"";
			 loadingMan = loading.split(",");
			 loginMan = loadingMan[0];
			 loading1 = loadingMan[1];
			 loading2 = loadingMan[2];
			 loading3 = loadingMan[3];
			 loading4 = loadingMan[4];
			 loading5 = loadingMan[5];
			 loading6 = loadingMan[6];
			 loading7 = loadingMan[7];
		}
		order.put(oderField, Integer.parseInt(oderType));
		if(mapKey1!=null){
			Map<String,Object> flowMap = (Map<String,Object>) JSON.parse(map1.get(mapKey1).toString());//实体数据
			DBObject[] value0 = new BasicDBObject[2];
			DBObject[] value1 = new BasicDBObject[2];
			DBObject[] value2 = new BasicDBObject[2];
			DBObject[] value3 = new BasicDBObject[2];
			List<String> listKey0 = new ArrayList<String>();
			List<String> listKey1 = new ArrayList<String>();
			List<String> listKey2 = new ArrayList<String>();
			List<String> listKey3 = new ArrayList<String>();
			List<String> listKey = new ArrayList<String>();
			if(!StringUtils.isEmpty(isRegex)){
				Set<Entry<String, Object>> set = flowMap.entrySet();
				for (Entry<String, Object> entry  :set) {
					mapKey = entry.getKey();
					if("DocumentNO".equals(mapKey)||"fromNO".equals(mapKey)){
						listKey0.add(mapKey);
					}
					else if("Title".equals(mapKey)||"RecordsTitle".equals(mapKey)){
						listKey1.add(mapKey);
					}
					else if("CreatedUserCN".equals(mapKey)||"ActiveUserCN".equals(mapKey)){
						listKey2.add(mapKey);
					}
					else if("drawupOrgshow".equals(mapKey)||"fromName".equals(mapKey)){
						listKey3.add(mapKey);
					}
					else{
						listKey.add(mapKey);
					}
				}
				if(listKey.size()>0){
					for (int i = 0; i < listKey.size(); i++) {
						if("is".equals(isRegex)){//精确查询
							searchQuery.put(listKey.get(i),flowMap.get(listKey.get(i))+""); 
							
						}else{//模糊查询
							pattern = Pattern.compile("^.*"+flowMap.get(listKey.get(i))+""+".*$", Pattern.CASE_INSENSITIVE); 
							searchQuery.put(listKey.get(i),pattern);
						}
						
					}
				}
				if(listKey0.size()>0){
					for (int i = 0; i < listKey0.size(); i++) {
						pattern = Pattern.compile("^.*"+flowMap.get(listKey0.get(i))+""+".*$", Pattern.CASE_INSENSITIVE); 
						value0[0] = new BasicDBObject("DocumentNO",pattern);  
						value0[1] = new BasicDBObject("fromNO", pattern);
					}
				}
				if(listKey1.size()>0){
					for (int i = 0; i < listKey1.size(); i++) {
						pattern = Pattern.compile("^.*"+flowMap.get(listKey1.get(i))+""+".*$", Pattern.CASE_INSENSITIVE); 
						value1[0] = new BasicDBObject("Title",pattern);  
						value1[1] = new BasicDBObject("RecordsTitle", pattern);
					}
				}
				if(listKey2.size()>0){
					for (int i = 0; i < listKey2.size(); i++) {
						pattern = Pattern.compile("^.*"+flowMap.get(listKey2.get(i))+""+".*$", Pattern.CASE_INSENSITIVE); 
						value2[0] = new BasicDBObject("CreatedUserCN",pattern);  
						value2[1] = new BasicDBObject("ActiveUserCN", pattern);
					}
				}
				if(listKey3.size()>0){
					for (int i = 0; i < listKey3.size(); i++) {
						pattern = Pattern.compile("^.*"+flowMap.get(listKey3.get(i))+""+".*$", Pattern.CASE_INSENSITIVE); 
						value3[0] = new BasicDBObject("drawupOrgshow",pattern);  
						value3[1] = new BasicDBObject("fromName", pattern);
					}
				}
			}
//			String sfw = searchQuery.getString("sfw");
//			if("^.*fw.*$".equals(sfw)){
			String title1 = null;
			String title2 = null;
			if(listKey1.size()>0){
				title1 = (String) flowMap.get(listKey1.get(0));
//			}else{
				title2 = (String) flowMap.get(listKey1.get(1));
			}
//			}
			if((title1==null||"".equals(title1))||(title2==null||"".equals(title2))){
				if( map1.get("isExists")!=null){
					String isExists = map1.get("isExists")+"";//判空字段
					String[] ex = isExists.split(",");
					for (int j = 0; j < ex.length; j++) {
						searchQuery.put(ex[j], new BasicDBObject("$ne",""));
					}
				}
			}
			searchQuery_or1.put(loading1, new BasicDBObject("$regex", loginMan+","));
			searchQuery_or2.put(loading1, new BasicDBObject("$regex", ","+loginMan+","));
			searchQuery_or3.put(loading1, new BasicDBObject("$regex", ","+loginMan));
			
			searchQuery_or.put(loading2, loginMan);
			searchQuery_or.put(loading3, loginMan);
			searchQuery_or.put(loading4, loginMan);
			searchQuery_or.put(loading5, loginMan);
			searchQuery_or.put(loading6, loginMan);
			searchQuery_or.put(loading7, loginMan);
			condList.add(searchQuery);//将这个查询条件放到条件集合中
			if(listKey0.size()>0){
				condList.add(new BasicDBObject("$or", value0));
			}
			if(listKey1.size()>0){
				condList.add(new BasicDBObject("$or", value1));
			}
			if(listKey2.size()>0){
				condList.add(new BasicDBObject("$or", value2));
			}
			if(listKey3.size()>0){
				condList.add(new BasicDBObject("$or", value3));
			}
			condList_or.add(searchQuery_or);
			condList_or.add(searchQuery_or1);
			condList_or.add(searchQuery_or2);
			condList_or.add(searchQuery_or3);
			condition= new BasicDBObject();//最后在将查询结果放到一个查询对象中去  
			condition.put("$and", condList);//多条件查询使用and  
			condition.put("$or", condList_or);//多条件查询使用and  
		}
		MongoClient mClient = null;
		DBCursor cursor = null;//对查询结果的接受  
	 	List<DBObject> list = null;
		try {
			mClient = new MongoClient("192.168.1.103",27017);
			DBCollection newsCollection  = mClient.getDB("hist").getCollection(mapKey1);
            cursor = newsCollection.find(condition).skip(pageNo*pageSize).limit(pageSize).sort(order);
            /*int pageMaxNum = newsCollection.find(condition).count();//得到所有的符合条件的数据，
            int pageMaxSize = pageMaxNum/pageSize;//符合条件的共有多少页
*/	       list = new ArrayList<DBObject>();
	        while(cursor.hasNext()){//如果游标中还有其他可供迭代的对象,就返回true
	        	DBObject obj = cursor.next();//为游标中中的下一个文档作为BDObject()返回,并将索引加一
	        	list.add(obj);
	            System.out.println(obj.toString());
	        }
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			cursor.close();
	        mClient.close();
		}
		 result = JSONObject.toJSON(list).toString(); 
		return result;
	}
	
	public List<String> queryAllList(String site) {
	 	MongoClient mClient = null;
	 	DBCursor cursor = null ;
	 	List<String> list = null;
		try {
			mClient = new MongoClient("192.168.1.103",27017);
			DB db = mClient.getDB("hist");
	        DBCollection collection = db.getCollection(site);
	        cursor = collection.find();
	       list = new ArrayList<String>();
	        while(cursor.hasNext()){
	        	DBObject obj = cursor.next();
	        	list.add(obj.toString());
	            System.out.println(obj.toString());
	        }
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			cursor.close();
	        mClient.close();
		}
        
		return list;
	}
	public List<Flow> queryBeanInfoBycondition(Map<String, Object> map1) {
		String mapKey1 = null;
		String mapKey = null;
		Pattern pattern;
		Query query = new Query();
		Criteria criteria = new Criteria();  
		Set<Entry<String, Object>> set1 = map1.entrySet();
		for (Entry<String, Object> entry : set1) {
			String k = entry.getKey();
			if(!"type".equals(k)&& !"isRegex".equals(k)&& !"pageNo".equals(k)&& !"pageSize".equals(k)&& !"".equals(k) && !StringUtils.isEmpty(k)){
				mapKey1 = k;
			}
		}
		Map<String,Object> flowMap = (Map<String,Object>) JSON.parse(map1.get(mapKey1).toString());//实体数据
    	String type = map1.get("type")+"";//是否分页
    	String isRegex = map1.get("isRegex")+"";//是否模糊查询
    	int pageNo = Integer.parseInt(map1.get("pageNo")+"");//当前页
    	int pageSize = Integer.parseInt(map1.get("pageSize")+"");//每页数量
		if(!StringUtils.isEmpty(isRegex)){
				List<String> listKey = new ArrayList<String>();
				Set<Entry<String, Object>> set = flowMap.entrySet();
				for (Entry<String, Object> entry  :set) {
					mapKey = entry.getKey();
					listKey.add(mapKey);
				}
				if(listKey.size()>0){
					for (int i = 0; i < listKey.size(); i++) {
						if("is".equals(isRegex)){//精确查询
							criteria.and(listKey.get(i)).is(flowMap.get(listKey.get(i))+"");  
						}else{//模糊查询
							pattern = Pattern.compile("^.*"+flowMap.get(listKey.get(i))+""+".*$", Pattern.CASE_INSENSITIVE); 
							criteria.and(listKey.get(i)).regex(pattern);  
						}
					}
				}
		}
		PageRequest req = null;
		query.addCriteria(criteria);
		if("page".equals(type)){
			req = new PageRequest(pageNo, pageSize);
		}
		List<Flow> listBean = mongoTemplate.find(query.with(req), Flow.class);
		return listBean;
	}
	public void updateOrSave(String str) {
		Query query = new Query();
		Criteria criteria = new Criteria();  
		Update update = new Update();
		String site = null;
		String siteValue = null;
		Map<String, Object> map = (Map<String, Object>) JSON.parse(str);
		Set<Entry<String, Object>> mEntry = map.entrySet();
		for (Entry<String, Object> entry : mEntry) {
			site = entry.getKey();
			siteValue = entry.getValue()+"";
		}
		Map<String, Object> map1 = (Map<String, Object>) JSON.parse(siteValue);
		Set<Entry<String, Object>> mEntry1 = map1.entrySet();
		for (Entry<String, Object> entry : mEntry1) {
			update.addToSet(entry.getKey(), entry.getValue());
			criteria=Criteria.where("_id").is(entry.getValue());
			query.addCriteria(criteria);
		}
		mongoTemplate.updateMulti(query, update,site);
	}
	public String checkMD5Info(String id) {
		MD5Value md5 = mongoTemplate.findById(id, MD5Value.class);
		String value = md5.getMD5();
		if(!StringUtils.isEmpty(value)){
			return "true";
		}
		return "false";
	}
	
	/* (非 Javadoc) 
	* <p>Title: queryPrevious</p> 
	* <p>Description: </p> 
	* @param map1
	* @return 
	* @see com.service.SaveAndqueryService#queryPrevious(java.util.Map) 
	*/
	public String queryPrevious(Map<String, Object> map1) {
		String result=null;
		BasicDBObject searchQuery = new BasicDBObject();
		BasicDBObject order = new BasicDBObject();
		BasicDBList condList = new BasicDBList();//存放查询条件的集合  
		BasicDBObject searchQuery_or = new BasicDBObject();
		BasicDBObject searchQuery_or1 = new BasicDBObject();
		BasicDBObject searchQuery_or2 = new BasicDBObject();
		BasicDBObject searchQuery_or3 = new BasicDBObject();
		BasicDBObject searchQuery_lt = new BasicDBObject();
		BasicDBList condList_or = new BasicDBList();//or
		BasicDBList condList_lt = new BasicDBList();//lt
		Pattern pattern;//用在模糊查询得时候，对字段进行匹配 
		String mapKey = null;
		String mapKey1 = null;
		BasicDBObject condition = null;
		Set<Entry<String, Object>> set1 = map1.entrySet();
		for (Entry<String, Object> entry : set1) {
			String k = entry.getKey();
			if(!"isExists".equals(k) &&!"loadingMan".equals(k) &&!"oderType".equals(k) &&!"oderField".equals(k) &&!"type".equals(k)&& !"isRegex".equals(k)&& !"oderType".equals(k)&& !"oderField".equals(k)&& !"pageNo".equals(k)&& !"pageSize".equals(k)&& !"".equals(k) && !StringUtils.isEmpty(k)){
				mapKey1 = k;
			}				
		}
		String type = map1.get("type")+"";//是否分页
		String isRegex = map1.get("isRegex")+"";//是否模糊查询
		int pageNo = Integer.parseInt(map1.get("pageNo")+"");//当前页
		int pageSize = Integer.parseInt(map1.get("pageSize")+"");//每页数量
		String oderField = map1.get("oderField")+"";//排序字段getTime
		String oderType = map1.get("oderType")+"";//倒序排序
		String sign = map1.get("sign")+"";//上一个下一个
		if("DESC".equals(oderType)){//降序 -1
			oderType = "-1";
		}else if("ASC".equals(oderType)){
			oderType = "1";
		}
		String loading=null;
		String[] loadingMan = null;
		String loginMan=null;
		String loading1=null;
		String loading2=null;
		String loading3=null;
		String loading4=null;
		String loading5=null;
		String loading6=null;
		String loading7=null;
		String getTime = null;
		if(map1.get("loadingMan")!=null){
			 loading = map1.get("loadingMan")+"";
			 loadingMan = loading.split(",");
			 loginMan = loadingMan[0];
			 loading1 = loadingMan[1];
			 loading2 = loadingMan[2];
			 loading3 = loadingMan[3];
			 loading4 = loadingMan[4];
			 loading5 = loadingMan[5];
			 loading6 = loadingMan[6];
			 loading7 = loadingMan[7];
		}
		if("pre".equals(sign)){
			order.put(oderField, 1);
		}else{
			order.put(oderField, -1);
		}
		if(mapKey1!=null){
			Map<String,Object> flowMap = (Map<String,Object>) JSON.parse(map1.get(mapKey1).toString());//实体数据
			if(!StringUtils.isEmpty(isRegex)){
				List<String> listKey = new ArrayList<String>();
				Set<Entry<String, Object>> set = flowMap.entrySet();
				for (Entry<String, Object> entry  :set) {
					mapKey = entry.getKey();
					listKey.add(mapKey);
				}
				if(listKey.size()>0){
					for (int i = 0; i < listKey.size(); i++) {
						if("is".equals(isRegex)){//精确查询
							searchQuery.put(listKey.get(i),flowMap.get(listKey.get(i))+""); 
							
						}else{//模糊查询
							if("getTime".equals(listKey.get(i))){
								getTime = (String) flowMap.get(listKey.get(i));
							}
							if("_id".equals(listKey.get(i))){
							}
							else{
								pattern = Pattern.compile("^.*"+flowMap.get(listKey.get(i))+""+".*$", Pattern.CASE_INSENSITIVE); 
								searchQuery.put(listKey.get(i),pattern);
							}
						}
						
					}
				}
			}
			String sfw = searchQuery.getString("sfw");
			String title;
			if("^.*fw.*$".equals(sfw)){
				title = searchQuery.getString("Title");
			}else{
				title = searchQuery.getString("RecordsTitle");
			}
			if("pre".equals(sign)){
				searchQuery.put("getTime", new BasicDBObject("$gt", getTime));
			}else{
				searchQuery.put("getTime", new BasicDBObject("$lt", getTime));
			}
			if(title==null||title.equals("^.*.*$")){
				if( map1.get("isExists")!=null){
					String isExists = map1.get("isExists")+"";//判空字段
					String[] ex = isExists.split(",");
					for (int j = 0; j < ex.length; j++) {
						searchQuery.put(ex[j], new BasicDBObject("$ne",""));
					}
				}
			}
			searchQuery_or1.put(loading1, new BasicDBObject("$regex", loginMan+","));
			searchQuery_or2.put(loading1, new BasicDBObject("$regex", ","+loginMan+","));
			searchQuery_or3.put(loading1, new BasicDBObject("$regex", ","+loginMan));
			searchQuery_or.put(loading2, loginMan);
			searchQuery_or.put(loading3, loginMan);
			searchQuery_or.put(loading4, loginMan);
			searchQuery_or.put(loading5, loginMan);
			searchQuery_or.put(loading6, loginMan);
			searchQuery_or.put(loading7, loginMan);
			condList.add(searchQuery);//将这个查询条件放到条件集合中 
			condList_or.add(searchQuery_or);
			condList_or.add(searchQuery_or1);
			condList_or.add(searchQuery_or2);
			condList_or.add(searchQuery_or3);
			//小于的对象
			searchQuery_lt.put("getTime", getTime);//val查询而来
			condList_lt.add(searchQuery_lt);//小于的集合
			condition= new BasicDBObject();//最后在将查询结果放到一个查询对象中去  
			condition.put("$and", condList);//多条件查询使用and  
			condition.put("$or", condList_or);//多条件查询使用and
		}
		MongoClient mClient = null;
		DBCursor cursor = null;//对查询结果的接受  
	 	List<DBObject> list = null;
		try {
			mClient = new MongoClient("192.168.1.103",27017);
			DBCollection newsCollection  = mClient.getDB("hist").getCollection(mapKey1);
            cursor = newsCollection.find(condition).limit(1).sort(order);
	        list = new ArrayList<DBObject>();
	        while(cursor.hasNext()){//如果游标中还有其他可供迭代的对象,就返回true
	        	DBObject obj = cursor.next();//为游标中中的下一个文档作为BDObject()返回,并将索引加一
	        	list.add(obj);
	            System.out.println(obj.toString());
	        }
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}finally{
			cursor.close();
	        mClient.close();
		}
		 result = JSONObject.toJSON(list).toString(); 
		return result;
	}
	public String queryNext(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}
}
