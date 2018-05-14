package com.xd.cheekat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xd.cheekat.dao.UserInfoDao;
import com.xd.cheekat.pojo.UserInfo;

@Service
public class UserInfoServiceImpl implements UserInfoService{

	@Autowired
	UserInfoDao userInfoDao;
	
	@Override
	public UserInfo selectByPrimaryKey(Long userId) {
		// TODO Auto-generated method stub
		return userInfoDao.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(UserInfo record) {
		// TODO Auto-generated method stub
		return userInfoDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<UserInfo> getUserByIds(String[] ids) {
		// TODO Auto-generated method stub
		return null;
	}

}