package com.payitnz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payitnz.dao.InfiUserDao;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.InfiSecurityQuestionsBean;
import com.payitnz.model.InfiUserFilesBean;
import com.payitnz.model.InfiUserSearchBean;
import com.payitnz.model.User;
@Service("UserService")
public class UserServiceImpl implements UserService{
	
	InfiUserDao userDao;
	
	@Autowired
	public void setInfiUserDao(InfiUserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public List<User> findByIDAndPassword(String username, String hashPassword) {
		return userDao.findByIDAndPassword(username,hashPassword);
	}

	@Override
	public boolean checkIfMerchantUserIdExists(int userId) {
		return userDao.checkIfMerchantUserIdExists(userId);
	}

	@Override
	public boolean CheckEmailId(String email) {
		return userDao.CheckEmailId(email);
	}

	@Override
	public boolean CheckUsername(String username) {
		return userDao.CheckUsername(username);
	}

	@Override
	public int save(User user) {
		return userDao.save(user);
	}

	@Override
	public void saveFiles(InfiUserFilesBean userFilesBean) {
		userDao.saveFiles(userFilesBean);
	}

	@Override
	public User findById(int id) {
		return userDao.findById(id);
	}

	@Override
	public int update(User user) {
		return userDao.update(user);
	}

	@Override
	public List<User> findUsersDetailsBySearchParams(InfiUserSearchBean search) {
		return userDao.findUsersDetailsBySearchParams(search);
	}

	@Override
	public void delete(int id) {
		userDao.delete(id);
	}

	@Override
	public List<InfiUserFilesBean> findAllUserFiles(int id) {
		return userDao.findAllUserFiles(id);
	}

	@Override
	public List<InfiUserFilesBean> getFileDetails(int id) {
		return userDao.getFileDetails(id);
	}

	@Override
	public void deleteFile(int id) {
		userDao.deleteFile(id);
	}
	
	@Override
	public int findByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
	@Override
	public void SaveAlipayRecord(AlipayWalletVO paybean) {
		userDao.SaveAlipayRecord(paybean);
	}
	
	@Override
	public AlipayWalletVO getAlipayRecord(AlipayWalletVO paybean) {
		return userDao. getAlipayRecord(paybean);
	}
	@Override
	public void UpdateAlipayRecord(AlipayWalletVO paybean) {
		userDao.UpdateAlipayRecord(paybean);
	}

	@Override
	public List<InfiSecurityQuestionsBean> findAllQuestions() {
		return userDao.findAllQuestions();
	}

	@Override
	public void saveSecurityQuestion(InfiSecurityQuestionsBean userForm) {
		userDao.saveSecurityQuestion(userForm);
	}
	
	@Override
	public AlipayWalletVO getAlipayOnlineRecord(AlipayWalletVO payConnection) {
		return userDao.getAlipayOnlineRecord(payConnection);
	}

	@Override
	public void SaveAlipayOnlineRecord(AlipayWalletVO payConnection) {
		userDao.SaveAlipayOnlineRecord(payConnection);
		
	}

	@Override
	public void UpdateAlipayOnlineRecord(AlipayWalletVO payConnection) {
		userDao.UpdateAlipayOnlineRecord(payConnection);
		
	}
}
