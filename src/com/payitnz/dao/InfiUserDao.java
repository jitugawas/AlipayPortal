package com.payitnz.dao;

import java.util.List;



import org.springframework.stereotype.Repository;

import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.EmailAlertsConfigBean;
import com.payitnz.model.InfiSecurityQuestionsBean;
import com.payitnz.model.InfiUserFilesBean;
import com.payitnz.model.InfiUserSearchBean;
import com.payitnz.model.User;

public interface InfiUserDao {

	List<User> findByIDAndPassword(String username, String hashPassword);
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

	void SaveAlipayRecord(AlipayWalletVO payData);
	AlipayWalletVO getAlipayRecord(AlipayWalletVO payConnection);
	void UpdateAlipayRecord(AlipayWalletVO payData);
	List<InfiSecurityQuestionsBean> findAllQuestions();
	void saveSecurityQuestion(InfiSecurityQuestionsBean userForm);
	boolean CheckUserPassword(String password);
	AlipayWalletVO getAlipayOnlineRecord(AlipayWalletVO payConnection);
	void SaveAlipayOnlineRecord(AlipayWalletVO payConnection);
	void UpdateAlipayOnlineRecord(AlipayWalletVO payConnection);
	List<EmailAlertsConfigBean> getAdminAlertConfigDetails(int userId);
	void saveAdminAlertConfigDetails(EmailAlertsConfigBean alertForm);
	void updateAdminAlertConfigDetails(EmailAlertsConfigBean alertForm);
	
}
