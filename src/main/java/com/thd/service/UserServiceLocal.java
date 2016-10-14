package com.thd.service;

import com.thd.bean.User;

public interface UserServiceLocal {
	/**
	 * 根据ID查询用户
	 * @param userId 用户id
	 * @return 用户对象
	 */
	public User findUser(String userId);
	
	public User findUserByName(String userName);
	/**
	 * 保存用户
	 * @param user 用户对象
	 */
	public void saveUser(User user);
}
