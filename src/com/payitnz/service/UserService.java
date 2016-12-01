package com.payitnz.service;

import java.util.List;

import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.InfiSecurityQuestionsBean;
import com.payitnz.model.InfiUserFilesBean;
import com.payitnz.model.InfiUserSearchBean;
import com.payitnz.model.User;

public interface UserService {

	List<User> findByIDAndPassword(String email, String hashPassword);
	boolean checkIfMerchantUserIdExists(int userId);
	boolean CheckEmailId(String email);
	boolean CheckUsername(String username);
	int save(User user);
	void saveFiles(InfiUserFilesBean userFilesBean);
	User findById(int id);
	int update(User user);
	List<User> findUsersDetailsBySearchParams(InfiUserSearchBean search);
	void delete(int id);
	List<InfiUserFilesBean> findAllUserFiles(int id);
	List<InfiUserFilesBean> getFileDetails(int id);
	void deleteFile(int id);
	int findByEmail(String email);

	void SaveAlipayRecord(AlipayWalletVO paybean);
	AlipayWalletVO getAlipayRecord(AlipayWalletVO paybean);
	void UpdateAlipayRecord(AlipayWalletVO paybean);
	List<InfiSecurityQuestionsBean> findAllQuestions();
	void saveSecurityQuestion(InfiSecurityQuestionsBean userForm);
	AlipayWalletVO getAlipayOnlineRecord(AlipayWalletVO payConnection);
	void SaveAlipayOnlineRecord(AlipayWalletVO payConnection);
	void UpdateAlipayOnlineRecord(AlipayWalletVO payConnection);
}
