package com.thd.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.core.util.Page;
import com.google.gson.Gson;
//import com.redis.RedisClientTemplate;
import com.thd.bean.User;
import com.thd.bean.UserCriteria;
import com.thd.dao.UserMapper;
import com.thd.service.UserService;

import redis.clients.jedis.JedisCluster;

@Service("userService")
public class UserServiceImpl implements UserService {
	private UserMapper userMapper;
	
//	@Autowired
//	private RedisClientTemplate redisClientTemplate;
	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	JedisCluster jedisCluster;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	public UserMapper getUserMapper() {
        return userMapper;
    }

	@Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
	
	@Override
	public User findUser(String userId) {
		log.info("Provider:findUser()");
		return userMapper.selectByPrimaryKey(Integer.valueOf(userId));
	}
	
	@Override
	public User findUserByName(String userName) {
//		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM tb2 a INNER JOIN tb3 b ON a.id=b.id ORDER BY a.id LIMIT 0,5 ");
//		for (Map<String, Object> a : list){
//			System.out.println("============================="+a);
//		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		UserCriteria ex = new UserCriteria();
		ex.createCriteria().andUserNameLike("%admin%");
		ex.setOrderByClause(" user_id desc ");
		System.out.println("count============================="+userMapper.countByCriteria(ex));
		
		Page page = new Page(0, 5);
		ex.setPage(page);
		page.setPageCount(userMapper.countByCriteria(ex));
		resultMap.put("page", page);
		
		List<User> list2 = userMapper.selectByCriteriaWithBLOBs(ex);
		resultMap.put("resultList", list2);
		for (User u : list2){
			System.out.println("============================="+u.getUserName());
		}
		
		User user = new User();
		UserCriteria ex2 = new UserCriteria();
		ex2.createCriteria().andUserIdEqualTo(123);
		userMapper.updateByCriteriaWithBLOBs(user, ex2);
		
		User uu = userMapper.selectByUserName(userName);
		log.info("Provider:findUser()");
		
		Gson gson = new Gson();
		String key = "user.userid." + uu.getUserId();
		System.out.println("key================"+key);;
		System.out.println("gson.toJson(uu)================"+gson.toJson(uu));;
		jedisCluster.set(key, gson.toJson(uu));
		
//		redisClientTemplate.set(key, gson.toJson(uu));
//		User expected = gson.fromJson(redisClientTemplate.get(key),User.class);
//		System.out.println("expected============================="+expected.getUserName());
		
		return uu;
	}

	@Override
	public void saveUser(User user) {
		log.info("Provider:saveUser()");
		System.out.println(" save user ...");
	}
	
	
}
