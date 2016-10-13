package com.thd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thd.bean.User;
import com.thd.bean.UserCriteria;

public interface UserMapper {
	
	User selectByPrimaryKey(Integer id);
	
	User selectByUserName(String userName);
	
	int deleteByPrimaryKey(Integer id);
	
	int insertSelective(User record);
	
	int updateByPrimaryKeySelective(User record);
	
	int countByCriteria(UserCriteria example);
	
	int deleteByCriteria(UserCriteria example);
	
	List<User> selectByCriteriaWithBLOBs(UserCriteria example);
	
	List<User> selectByCriteria(UserCriteria example);
	
	int updateByCriteriaWithBLOBs(@Param("record") User record, @Param("update") UserCriteria example);
	
}