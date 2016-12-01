package com.payitnz.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class SpringDBConfig {

	@Autowired
	DataSource dataSource;

	@Bean
	public JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	//Local database configuration
	/*@Bean
        public DataSource getDataSource() {
                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                dataSource.setUrl("jdbc:mysql://localhost:3306/contactdb");
                dataSource.setUsername("root");
                dataSource.setPassword("admin");
                
                return dataSource;
        }*/
	
	//Remote database configuration
     /* @Bean
      public DataSource getDataSource() {
             DriverManagerDataSource dataSource = new DriverManagerDataSource();
             dataSource.setDriverClassName("com.mysql.jdbc.Driver");
             dataSource.setUrl("jdbc:mysql://mysql1000.mochahost.com:3306/tehmus_pilotdpa");
             dataSource.setUsername("tehmus_pilotuser");
              dataSource.setPassword("pilotuser@2016");
              return dataSource;              
     }
     */
      
	      
	    @Bean
	    public DataSource getDataSource() {
	           DriverManagerDataSource dataSource = new DriverManagerDataSource();
	           dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	           dataSource.setUrl(DynamicPaymentConstant.DB_URL);
	           dataSource.setUsername(DynamicPaymentConstant.DB_USER);
	           dataSource.setPassword(DynamicPaymentConstant.DB_PASSWORD);
	            return dataSource;              
	   }
	
	/*  @Bean
      public DataSource getDataSource() {
              DriverManagerDataSource dataSource = new DriverManagerDataSource();
              dataSource.setDriverClassName("com.mysql.jdbc.Driver");
              dataSource.setUrl("jdbc:mysql://localhost:3306/tehmus_pilotdpa");
              dataSource.setUsername("root");
              dataSource.setPassword("");

              return dataSource;
      }
	*/

}