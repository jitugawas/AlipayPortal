package com.payitnz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.EmailAlertsConfigBean;
import com.payitnz.model.InfiSecurityQuestionsBean;
import com.payitnz.model.InfiUserFilesBean;
import com.payitnz.model.InfiUserSearchBean;
import com.payitnz.model.User;

@Repository
public class InfiUserDaoImpl implements InfiUserDao{

	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	JdbcTemplate jdbcTemplate;

	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<User> findByIDAndPassword(String username, String hashPassword) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", hashPassword);

		String sql = "SELECT * FROM users_payitnz WHERE (username=:username OR  email=:username) AND infidigiPassword=:password ";
		List<User> result = null;
		try {
			result = namedParameterJdbcTemplate.query(sql, params,new UserMapper());

		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return  result;
	}

	private static final class UserMapper implements RowMapper<User>{

		@Override
		public User mapRow(ResultSet rs, int ag) throws SQLException {
			User infiBean = new User();
			infiBean.setCompanyName(rs.getString("companyName"));
			infiBean.setEmail(rs.getString("email"));
			infiBean.setFirstName(rs.getString("firstName"));
			infiBean.setLastName(rs.getString("lastName"));
			infiBean.setInfidigiAccountId(rs.getString("infidigiAccountId"));
			infiBean.setId(rs.getInt("id"));
			infiBean.setInfidigiUserId(rs.getString("infidigiUserId"));
			infiBean.setInfidigiPassword(rs.getString("infidigiPassword"));
			infiBean.setPhoneNo(rs.getString("phoneNo"));
			infiBean.setRoleId(rs.getInt("roleId"));
			infiBean.setStatus(rs.getString("status"));
			infiBean.setUsername(rs.getString("username"));
			infiBean.setCreated_date(rs.getString("created_date"));
			infiBean.setVerified(rs.getInt("verified"));
			infiBean.setLast_logged_in_at(rs.getString("last_logged_in_at"));
			infiBean.setPermission_access_app(rs.getInt("permission_access_app"));
			infiBean.setAll_permission(rs.getInt("all_permission"));
			infiBean.setPermission_setup_merchant(rs.getInt("permission_setup_merchant"));
			infiBean.setPermission_reconciliation(rs.getInt("permission_reconciliation"));
			infiBean.setPermission_refund(rs.getInt("permission_refund"));
			infiBean.setPermission_settlement(rs.getInt("permission_settlement"));
			infiBean.setPermission_setup_users(rs.getInt("permission_setup_users"));
			infiBean.setPermission_setup_connections(rs.getInt("permission_setup_connections"));
			infiBean.setPermission_alipay_transactions(rs.getInt("permission_alipay_transactions"));
			return infiBean;
		}

	}

	@Override
	public boolean checkIfMerchantUserIdExists(int userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);

		String sql = "SELECT * FROM users_payitnz WHERE infidigiUserId=:userId";
		boolean result = true;
		try {
			List<User>  userData = namedParameterJdbcTemplate.query(sql, params,new UserMapper());
			if(userData.isEmpty()){
				result = false;
			}     
		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return  result;
	}

	@Override
	public boolean CheckEmailId(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email",email);

		String sql = "SELECT * FROM users_payitnz WHERE email=:email";
		boolean result = true;
		try {
			List<User>  userData = namedParameterJdbcTemplate.query(sql, params,new UserMapper());
			if(userData.isEmpty()){
				result = false;
			}     
		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return  result;
	}

	@Override
	public boolean CheckUsername(String username) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username",username);

		String sql = "SELECT * FROM users_payitnz WHERE username=:username";
		boolean result = true;
		try {
			List<User>  userData = namedParameterJdbcTemplate.query(sql, params,new UserMapper());
			if(userData.isEmpty()){
				result = false;
			}     
		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return  result;
	}

	@Override
	public int save(User user) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		String sql = "INSERT INTO users_payitnz(firstName, lastName,infidigiUserId,infidigiAccountId,infidigiPassword, username,companyName, phoneNo, email, roleId, creator_id, status, created_date,all_permission,permission_alipay_transactions,permission_refund,permission_setup_merchant,permission_setup_users,permission_setup_connections,permission_access_app,permission_settlement,permission_reconciliation) " + "VALUES (:firstName, :lastName, :infidigiUserId, :infidigiAccountId , :infidigiPassword, :username , :companyName, :phoneNo, :email, :roleId,:creator_id, :status, :created_date, :all_permission, :permission_alipay_transactions, :permission_refund, :permission_setup_merchant, :permission_setup_users, :permission_setup_connections, :permission_access_app,:permission_settlement,:permission_reconciliation)";

		namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(user), keyHolder);
		user.setId(keyHolder.getKey().intValue());
		return user.getId();
	}

	private SqlParameterSource getSqlParameterByModel(User user) {

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("id", user.getId());
		paramSource.addValue("firstName", user.getFirstName());
		paramSource.addValue("lastName", user.getLastName());
		paramSource.addValue("infidigiUserId",user.getInfidigiUserId());
		paramSource.addValue("infidigiAccountId",user.getInfidigiAccountId());
		paramSource.addValue("infidigiPassword", user.getInfidigiPassword());
		paramSource.addValue("companyName",user.getCompanyName());
		paramSource.addValue("username",user.getUsername());
		paramSource.addValue("phoneNo", user.getPhoneNo());
		paramSource.addValue("email", user.getEmail());
		paramSource.addValue("roleId", user.getRoleId());
		paramSource.addValue("creator_id", user.getCreator_id());
		paramSource.addValue("status", user.getStatus());
		paramSource.addValue("created_date", user.getCreated_date());
		paramSource.addValue("verified", user.getVerified());
		paramSource.addValue("last_logged_in_at", user.getLast_logged_in_at());
		paramSource.addValue("all_permission",user.getAll_permission());
		paramSource.addValue("permission_alipay_transactions", user.getPermission_alipay_transactions());
		paramSource.addValue("permission_refund",user.getPermission_refund());
		paramSource.addValue("permission_setup_merchant",user.getPermission_setup_merchant());
		paramSource.addValue("permission_setup_users",user.getPermission_setup_users());
		paramSource.addValue("permission_setup_connections", user.getPermission_setup_connections());
		paramSource.addValue("permission_access_app",user.getPermission_access_app());
		paramSource.addValue("permission_settlement",user.getPermission_settlement());
		paramSource.addValue("permission_reconciliation",user.getPermission_reconciliation());
		
		//paramSource.addValue("address", user.getAddress());
		// paramSource.addValue("country", user.getCountry());

		return paramSource;
	}

	@Override
	public void saveFiles(InfiUserFilesBean userFilesBean) {
		// TODO Auto-generated method stub
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO merchant_files(user_id,file_name,file_description,created_date) " + "VALUES (:user_id, :file_name, :file_description, :created_date)";
		namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(userFilesBean), keyHolder);
	}

	private SqlParameterSource getSqlParameterByModel(InfiUserFilesBean files) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("user_id",files.getUser_id());
		paramSource.addValue("file_name",files.getFile_name());
		paramSource.addValue("file_description",files.getFile_description());
		paramSource.addValue("created_date",files.getCreated_date());
		return paramSource;
	}

	@Override
	public User findById(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		String sql = "SELECT * FROM users_payitnz WHERE id=:id";
		List<User> result = null;
		try {
			result = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
			for (User UsersBean : result) {
				return UsersBean;
			}

		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return null;
	}

	@Override
	public int update(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", user.getId());
		String sql = "UPDATE users_payitnz SET firstName=:firstName, lastName=:lastName, " + "companyName=:companyName,infidigiPassword=:infidigiPassword, phoneNo=:phoneNo,status=:status, email=:email,verified=:verified,last_logged_in_at=:last_logged_in_at,all_permission=:all_permission,permission_alipay_transactions=:permission_alipay_transactions,permission_refund=:permission_refund,permission_setup_merchant=:permission_setup_merchant,permission_setup_users=:permission_setup_users,permission_setup_connections=:permission_setup_connections,permission_access_app=:permission_access_app,permission_settlement=:permission_settlement,permission_reconciliation=:permission_reconciliation WHERE id=:id";

		namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(user));
		return user.getId();
	}

	@Override
	public List<User> findUsersDetailsBySearchParams(InfiUserSearchBean search) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "SELECT * FROM users_payitnz ";
		int i=0;

		if(search.getSearch_string() !=null && !search.getSearch_string().isEmpty()){
			params.put("search_string",search.getSearch_string());
			i++;
			if(i==1){
				sql+= " Where (firstName REGEXP:search_string OR lastName REGEXP:search_string OR email REGEXP:search_string OR phoneNo REGEXP:search_string) ";
			}else{
				sql+= " AND (firstName REGEXP:search_string OR lastName REGEXP:search_string OR email REGEXP:search_string OR phoneNo REGEXP:search_string)";
			}
		}

		if(search.getStatus() !=null && !search.getStatus().isEmpty()){
			params.put("status",search.getStatus());
			i++;
			if(i==1){
				sql+= " Where status IN(:status)";
			}else{
				sql+= " AND status IN(:status)";
			}
		}

		if(search.getRole_id() == 2){
			params.put("user_id",search.getUser_id());
			i++;
			if(i==1){
				sql+= " Where creator_id = :user_id ";
			}else{
				sql+= " AND creator_id = :user_id ";
			}
		}

		if(search.getRole_id() == 1){
			i++;
			if(i==1){
				if(search.getDisplay_user() == 1){
					sql+= " Where roleId IN(2) ";
				}else{
					sql+= " Where roleId IN(3) ";
				}

			}else{
				if(search.getDisplay_user() == 1){
					sql+= " AND roleId IN (2) ";
				}else{
					sql+= " AND roleId IN (3) ";
				}
			}
		}

		sql+= " ORDER BY firstName";

		List<User> result = null;
		try {
			result = namedParameterJdbcTemplate.query(sql, params,new UserMapper());

		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return  result;
	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM users_payitnz WHERE id= :id";
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("id", id));


	}

	@Override
	public List<InfiUserFilesBean> findAllUserFiles(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String sql = "SELECT * FROM merchant_files where user_id =:id";
		List<InfiUserFilesBean> result = null;
		try{
			result = namedParameterJdbcTemplate.query(sql,params, new FileMapper());
		}catch(Exception e){

		}

		return result;
	}

	private static final class FileMapper implements RowMapper<InfiUserFilesBean> {

		public InfiUserFilesBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			InfiUserFilesBean file = new InfiUserFilesBean();
			file.setId(rs.getInt("id"));
			file.setFile_name(rs.getString("file_name"));
			file.setFile_description(rs.getString("file_description"));
			file.setUser_id(rs.getInt("user_id"));
			file.setCreated_date(rs.getString("created_date"));
			return file;
		}

	}

	@Override
	public List<InfiUserFilesBean> getFileDetails(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String sql = "SELECT * FROM merchant_files where id =:id";
		List<InfiUserFilesBean> result = null;
		try{
			result = namedParameterJdbcTemplate.query(sql,params, new FileMapper());
		}catch(Exception e){

		}

		return result;
	}

	@Override
	public void deleteFile(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String sql = "Delete From merchant_files where id =:id";
		namedParameterJdbcTemplate.update(sql,params);
	}

	@Override
	public int findByEmail(String email) {
		int userId = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email",email);

		String sql = "SELECT * FROM users_payitnz WHERE email=:email";
		List<User> result = null;
		try {
			result = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
			for(User res:result){
				userId = res.getId();
			}

		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return userId;
	}


	@Override
	public void SaveAlipayRecord(AlipayWalletVO payData) {

		KeyHolder keyHolder = new GeneratedKeyHolder();

		String sql ="INSERT INTO alipay_configuration(user_id, service, alipay_partner_ID, alipay_partner_key,character_set,currency, return_url,created_date) " +
				"VALUES (:user_id, :service, :alipay_partner_ID,:alipay_partner_key,:character_set, :currency, :return_url, :created_date)";

		namedParameterJdbcTemplate.update(sql, getSqlParameterByModelAlipay(payData), keyHolder);
		payData.setId(keyHolder.getKey().intValue());

	}
	private SqlParameterSource getSqlParameterByModelAlipay(AlipayWalletVO payInfo) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("id", payInfo.getId());
		paramSource.addValue("user_id", payInfo.getUser_id());
		paramSource.addValue("service", payInfo.getService());
		paramSource.addValue("alipay_partner_ID", payInfo.getPayitnz_id());
		paramSource.addValue("alipay_partner_key", payInfo.getPartner_key());
		paramSource.addValue("character_set", payInfo.getCharSet());
		//	        paramSource.addValue("notification_url", payInfo.getAlipay_notification_url());
		paramSource.addValue("return_url", payInfo.getReturn_url());
		//	        paramSource.addValue("order_valid_time", payInfo.getOrder_valid_time());
		paramSource.addValue("currency", payInfo.getCurrency());
		//	        paramSource.addValue("merchant_data", payInfo.getMerchant_data());
		//	        paramSource.addValue("supported_method", payInfo.getAlipay_supported_method());
		paramSource.addValue("created_date", payInfo.getCreated_date());
		//	        paramSource.addValue("status", payInfo.getstatus());

		return paramSource;
	}


	@Override
	public AlipayWalletVO getAlipayRecord(AlipayWalletVO payConnection) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", payConnection.getUser_id());

		String sql = "SELECT * FROM alipay_configuration WHERE user_id=:id ";
		List<AlipayWalletVO> result = null;
		try {

			result = namedParameterJdbcTemplate.query(sql, params, new AlipayMapper());
			if(result.size() == 0) {
				return null;
			} else if(result.size() >=1) {
				for (AlipayWalletVO paymentMethodInfo : result) {

					return paymentMethodInfo;
				}

			} 
		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return null;
	}

	private static final class AlipayMapper implements RowMapper<AlipayWalletVO> {

		public AlipayWalletVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AlipayWalletVO Alipaygateway = new AlipayWalletVO();
			Alipaygateway.setId(rs.getInt("id"));
			Alipaygateway.setUser_id(rs.getString("user_id"));
			Alipaygateway.setPayitnz_id(rs.getString("alipay_partner_ID"));
			Alipaygateway.setService(rs.getString("service"));
			Alipaygateway.setPartner_key(rs.getString("alipay_partner_key"));
			Alipaygateway.setCurrency(rs.getString("currency"));
			Alipaygateway.setCharSet(rs.getString("character_set"));
			Alipaygateway.setReturn_url(rs.getString("return_url"));
			Alipaygateway.setCreated_date(rs.getTimestamp("created_date"));
			//		    	   poligateway.set(rs.getString("failure_url"));
			//		    	   poligateway.setFi_code(rs.getString("created_date"));

			return Alipaygateway;

		}

	}


	@Override
	public void UpdateAlipayRecord(AlipayWalletVO payData) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", payData.getId());
		String sql ="UPDATE alipay_configuration SET service=:service, alipay_partner_ID=:alipay_partner_ID, alipay_partner_key=:alipay_partner_key, character_set=:character_set, currency=:currency, return_url=:return_url WHERE id=:id";
		namedParameterJdbcTemplate.update(sql, getSqlParameterByModelAlipay(payData));
	}

	@Override
	public List<InfiSecurityQuestionsBean> findAllQuestions() {
		String sql = "SELECT * FROM pnz_security_questions";
        List<InfiSecurityQuestionsBean> result = namedParameterJdbcTemplate.query(sql, new QuestionsMapper());

        return result;
	}
	
	private static final class QuestionsMapper implements RowMapper<InfiSecurityQuestionsBean> {

        public InfiSecurityQuestionsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
        	InfiSecurityQuestionsBean question = new InfiSecurityQuestionsBean();
        	question.setId(rs.getInt("id"));
        	question.setSecurity_question(rs.getString("security_question"));
            return question;
        }
    }

	@Override
	public void saveSecurityQuestion(InfiSecurityQuestionsBean userForm) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
	        String sql = "INSERT INTO pnz_user_security_questions(user_id, question_id, answer, created_date) " + "VALUES (:user_id, :question_id, :answer, :created_date)";
	        namedParameterJdbcTemplate.update(sql, getUserQuestionsMapper(userForm), keyHolder);
	        userForm.setId(keyHolder.getKey().intValue());
		
	}
	
	 private SqlParameterSource getUserQuestionsMapper(InfiSecurityQuestionsBean question) {

	        MapSqlParameterSource paramSource = new MapSqlParameterSource();
	        paramSource.addValue("id", question.getId());
	        paramSource.addValue("user_id", question.getUser_id());
	    
	        paramSource.addValue("question_id", question.getQuestion_id());
	        if(question.getAnswer()!="")
	        {
	            paramSource.addValue("answer", question.getAnswer());

	        }
	        else
	        {
	            paramSource.addValue("answer", question.getAnswer1());
	        }
	        paramSource.addValue("created_date", question.getCreated_date());
	          
	        return paramSource;
	    }

	@Override
	public boolean CheckUserPassword(String password) {
		 Map<String, Object> params = new HashMap<String, Object>();
	        params.put("password", password);
	        
	        String sql = "SELECT * FROM users_payitnz WHERE infidigiPassword =:password";
	        List<User> result = null;
	        try {
	            result = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
	            
	            if(result.size() >= 1) {
	                return true;
	            } else{
	                return false;
	            } 
	        } catch (EmptyResultDataAccessException e) {
	            // do nothing, return null
	        }
	        return false;
	}
	
	@Override
	public AlipayWalletVO getAlipayOnlineRecord(AlipayWalletVO payConnection) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", payConnection.getUser_id());

		String sql = "SELECT * FROM alipay_online_configuration WHERE user_id=:id ";
		List<AlipayWalletVO> result = null;
		try {

			result = namedParameterJdbcTemplate.query(sql, params, new AlipayOnlineMapper());
			if(result.size() == 0) {
				return null;
			} else if(result.size() >=1) {
				for (AlipayWalletVO paymentMethodInfo : result) {
					return paymentMethodInfo;
				}
			} 
		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return null;
	}

	@Override
	public void SaveAlipayOnlineRecord(AlipayWalletVO payConnection) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		String sql ="INSERT INTO alipay_online_configuration(user_id, online_service, alipay_online_partner_id, alipay_online_partner_key,online_character_set,online_currency, alipay_online_return_url,alipay_online_notification_url,order_valid_time,alipay_supported_method,online_status,subject,sign_type,url,alipay_online_created_date) " +
				"VALUES (:user_id, :online_service, :alipay_online_partner_id, :alipay_online_partner_key,:online_character_set,:online_currency, :alipay_online_return_url,:alipay_online_notification_url,:order_valid_time,:alipay_supported_method,:online_status,:subject,:sign_type,:url,:alipay_online_created_date)";

		namedParameterJdbcTemplate.update(sql, getSqlParameterByModelAlipayOnline(payConnection), keyHolder);
		payConnection.setId(keyHolder.getKey().intValue());
		
	}

	private SqlParameterSource getSqlParameterByModelAlipayOnline(AlipayWalletVO payInfo) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("id", payInfo.getId());
		paramSource.addValue("user_id", payInfo.getUser_id());
		paramSource.addValue("online_service", payInfo.getOnline_service());
		paramSource.addValue("alipay_online_partner_id", payInfo.getAlipay_online_partner_id());
		paramSource.addValue("alipay_online_partner_key", payInfo.getAlipay_online_partner_key());
		paramSource.addValue("online_character_set", payInfo.getOnline_character_set());
		paramSource.addValue("online_currency", payInfo.getOnline_currency());
		paramSource.addValue("alipay_online_return_url", payInfo.getAlipay_online_return_url());
		paramSource.addValue("alipay_online_notification_url", payInfo.getAlipay_online_notification_url());
		paramSource.addValue("order_valid_time", payInfo.getOrder_valid_time());
		paramSource.addValue("alipay_supported_method", payInfo.getAlipay_supported_method());
		paramSource.addValue("online_status",payInfo.isOnline_status());
		paramSource.addValue("subject",payInfo.getSubject());
		paramSource.addValue("sign_type",payInfo.getSign_type());
		paramSource.addValue("url",payInfo.getUrl());
		paramSource.addValue("alipay_online_created_date",payInfo.getAlipay_online_created_date());
		
		return paramSource;
	}

	@Override
	public void UpdateAlipayOnlineRecord(AlipayWalletVO payConnection) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", payConnection.getId());
		String sql ="UPDATE alipay_online_configuration SET online_service=:online_service, alipay_online_partner_id=:alipay_online_partner_id, alipay_online_partner_key=:alipay_online_partner_key, online_character_set=:online_character_set, online_currency=:online_currency, alipay_online_return_url=:alipay_online_return_url,alipay_online_notification_url=:alipay_online_notification_url,order_valid_time=:order_valid_time,alipay_supported_method=:alipay_supported_method WHERE id=:id";
		namedParameterJdbcTemplate.update(sql, getSqlParameterByModelAlipayOnline(payConnection));	
	}
	
	private static final class AlipayOnlineMapper implements RowMapper<AlipayWalletVO> {

		public AlipayWalletVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AlipayWalletVO Alipaygateway = new AlipayWalletVO();
			Alipaygateway.setId(rs.getInt("id"));
			Alipaygateway.setUser_id(rs.getString("user_id"));
			Alipaygateway.setOnline_service(rs.getString("online_service"));
			Alipaygateway.setAlipay_online_partner_id(rs.getString("alipay_online_partner_id"));
			Alipaygateway.setAlipay_online_partner_key(rs.getString("alipay_online_partner_key"));
			Alipaygateway.setOnline_character_set(rs.getString("online_character_set"));
			Alipaygateway.setOnline_currency(rs.getString("online_currency"));
			Alipaygateway.setAlipay_online_return_url(rs.getString("alipay_online_return_url"));
			Alipaygateway.setOrder_valid_time(rs.getInt("order_valid_time"));
			Alipaygateway.setAlipay_supported_method(rs.getInt("alipay_supported_method"));
			Alipaygateway.setOnline_status(rs.getBoolean("online_status"));
			Alipaygateway.setAlipay_online_notification_url(rs.getString("alipay_online_notification_url"));
			Alipaygateway.setSubject(rs.getString("subject"));
			Alipaygateway.setSign_type(rs.getString("sign_type"));
			Alipaygateway.setUrl(rs.getString("url"));
			Alipaygateway.setAlipay_online_created_date(rs.getTimestamp("alipay_online_created_date"));
		
			return Alipaygateway;

		}

	}
	
	@Override
	public List<EmailAlertsConfigBean> getAdminAlertConfigDetails(int userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", userId);
		String sql = "SELECT * FROM pnz_admin_email_alerts_config where user_id =:id";
		List<EmailAlertsConfigBean> result = null;
		try{
			result = namedParameterJdbcTemplate.query(sql,params, new AlertMapper());
		}catch(Exception e){

		}

		return result;
	}
	
	private static final class AlertMapper implements RowMapper<EmailAlertsConfigBean> {

		public EmailAlertsConfigBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailAlertsConfigBean alertBean = new EmailAlertsConfigBean();
			alertBean.setId(rs.getInt("id"));
			alertBean.setUser_id(rs.getInt("user_id"));
			alertBean.setIs_reconciliation_failure_emails(rs.getInt("is_reconciliation_failure_emails"));
			alertBean.setIs_reconciliation_success_emails(rs.getInt("is_reconciliation_success_emails"));
			alertBean.setIs_settle_reconcile_phone_number(rs.getInt("is_settle_reconcile_phone_number"));
			alertBean.setIs_settlement_failure_emails(rs.getInt("is_settlement_failure_emails"));
			alertBean.setIs_settlement_success_emails(rs.getInt("is_settlement_success_emails"));
			alertBean.setReconciliation_failure_emails(rs.getString("reconciliation_failure_emails"));
			alertBean.setReconciliation_success_emails(rs.getString("reconciliation_success_emails"));
			alertBean.setSettle_reconcile_phone_number(rs.getString("settle_reconcile_phone_number"));
			alertBean.setSettlement_failure_emails(rs.getString("settlement_failure_emails"));
			alertBean.setSettlement_success_emails(rs.getString("settlement_success_emails"));
			return alertBean;
		}
	}

	@Override
	public void saveAdminAlertConfigDetails(EmailAlertsConfigBean alertForm) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		String sql ="INSERT INTO pnz_admin_email_alerts_config(user_id, is_reconciliation_failure_emails, is_reconciliation_success_emails, is_settle_reconcile_phone_number,is_settlement_failure_emails,is_settlement_success_emails, reconciliation_failure_emails,reconciliation_success_emails,settle_reconcile_phone_number,settlement_failure_emails,settlement_success_emails) " +
				"VALUES (:user_id, :is_reconciliation_failure_emails, :is_reconciliation_success_emails, :is_settle_reconcile_phone_number,:is_settlement_failure_emails,:is_settlement_success_emails, reconciliation_failure_emails,:reconciliation_success_emails,:settle_reconcile_phone_number,:settlement_failure_emails,:settlement_success_emails)";

		namedParameterJdbcTemplate.update(sql, getSqlParameterByModelEmailAlerts(alertForm), keyHolder);
		alertForm.setId(keyHolder.getKey().intValue());
	}

	private SqlParameterSource getSqlParameterByModelEmailAlerts(EmailAlertsConfigBean alertForm) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();	
		paramSource.addValue("id", alertForm.getId());
		paramSource.addValue("user_id", alertForm.getUser_id());
		paramSource.addValue("is_reconciliation_failure_emails", alertForm.getIs_reconciliation_failure_emails());
		paramSource.addValue("is_reconciliation_success_emails", alertForm.getIs_reconciliation_success_emails());
		paramSource.addValue("is_settle_reconcile_phone_number", alertForm.getIs_settle_reconcile_phone_number());
		paramSource.addValue("is_settlement_failure_emails", alertForm.getIs_settlement_failure_emails());
		paramSource.addValue("is_settlement_success_emails", alertForm.getIs_settlement_success_emails());
		paramSource.addValue("reconciliation_failure_emails", alertForm.getReconciliation_failure_emails());
		paramSource.addValue("reconciliation_success_emails", alertForm.getReconciliation_success_emails());
		paramSource.addValue("settle_reconcile_phone_number", alertForm.getSettle_reconcile_phone_number());
		paramSource.addValue("settlement_failure_emails", alertForm.getSettlement_failure_emails());
		paramSource.addValue("settlement_success_emails", alertForm.getSettlement_success_emails());
		return paramSource;
	}

	@Override
	public void updateAdminAlertConfigDetails(EmailAlertsConfigBean alertForm) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", alertForm.getId());
		String sql ="UPDATE pnz_admin_email_alerts_config SET  is_reconciliation_failure_emails=:is_reconciliation_failure_emails, is_reconciliation_success_emails=:is_reconciliation_success_emails, is_settle_reconcile_phone_number=:is_settle_reconcile_phone_number,is_settlement_failure_emails=:is_settlement_failure_emails,is_settlement_success_emails=:is_settlement_success_emails,reconciliation_failure_emails=:reconciliation_failure_emails,reconciliation_success_emails=:reconciliation_success_emails,settle_reconcile_phone_number=:settle_reconcile_phone_number,settlement_failure_emails=:settlement_failure_emails,settlement_success_emails=:settlement_success_emails WHERE id=:id";
		namedParameterJdbcTemplate.update(sql, getSqlParameterByModelEmailAlerts(alertForm));		
	}
}
