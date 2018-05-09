package com.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.model.Flow;

public class queryModelInfo extends templateCon<Flow>{
	@Autowired
    private MongoTemplate mongoTemplate;
	@Override
	public List<Flow>  queryByCon(Query query, Class<Flow> flow) {
		return null;
	}
	@Override
	public boolean insertBeans(List<Flow> list,Class<Flow> t) {
		 if(list.size()>0){
			 mongoTemplate.insert(list,t);
			 return true;
		 }
		return false;
	}


}
