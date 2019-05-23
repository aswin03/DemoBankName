package com.edubank.dao;

import java.security.NoSuchAlgorithmException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edubank.entity.CustomerLoginEntity;
import com.edubank.model.CustomerLogin;
import com.edubank.utility.Hashing;

/**
 * This class contains the methods responsible for interacting the database
 * with respect to Login module like getLoginOfCustomer, changeCustomerPassword.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "customerloginDao")
public class CustomerLoginDAOImpl implements CustomerLoginDAO {

	@Autowired
	SessionFactory sessionFactory;

	/**
	 * This method is used to get a CustomerLogin model corresponding to its
	 * loginName<br>
	 * It uses CriteriaBuilder to interact with database for fetching the data
	 * 
	 * @param loginName
	 * 
	 * @return customerLogin
	 */
	@Override
	public CustomerLogin getCustomerLoginByLoginName(String loginName) throws Exception {
		
		loginName = loginName.toLowerCase();
		CustomerLogin customerLogin = null;
				
		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();

			/*
			 * If we are getting the session through openSession and we need to make
			 * a transaction to the database, we need to begin a transaction. As we
			 * have opened a new transaction, we don't need to check if the
			 * transaction is already active or not.
			 */

			/* Create a builder which is used to create criteria. 
			 * This builder contains many methods for arithmetic, logical and other operations */
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			
			/* Creating the criteria query specifying the required output i.e. CustomerLoginEntity */
			CriteriaQuery<CustomerLoginEntity> criteriaQuery = criteriaBuilder.createQuery(CustomerLoginEntity.class);
			
			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<CustomerLoginEntity> root = criteriaQuery.from(CustomerLoginEntity.class);
			
			/* Get the property loginName using root 
			 * equals method can be used of builder for comparing */
			criteriaQuery.where(criteriaBuilder.equal(root.get("loginName"), loginName));
			
			/*
			 * createQuery: is used to Convert the Criteria to executable Query which can be queried
			 * uiqueResult: is used to execute the query and get single entity instead of list of entities
			 *  */
			CustomerLoginEntity customerLoginEntity = session.createQuery(criteriaQuery).uniqueResult();
			
			/* here we are getting values from entity class and setting it to bean class */
			if (customerLoginEntity != null) {
				customerLogin = new CustomerLogin();
				customerLogin.setCustomerLoginId(customerLoginEntity.getCustomerLoginId());
				customerLogin.setCustomerId(customerLoginEntity.getCustomerId());
				customerLogin.setLoginName(customerLoginEntity.getLoginName());
				customerLogin.setLockedStatus(customerLoginEntity.getLockedStatus());
				customerLogin.setPassword(customerLoginEntity.getPassword());
			}
			
		return customerLogin;
	}

	/**
	 * This method is used to get a CustomerLogin model corresponding to its
	 * customerId<br>
	 * It uses CriteriaBuilder to interact with database for fetching the data
	 * 
	 * @param customerId
	 * 
	 * @return customerLogin
	 */
	@Override
	public CustomerLogin getCustomerLoginByCustomerId(Integer customerId) throws Exception {
		CustomerLogin customerLogin = null;
		
		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();

			
			
			/* Create a builder which is used to create criteria. 
			 * This builder contains many methods for arithmetic, logical and other operations */
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			
			/* Creating the criteria query specifying the required output i.e. CustomerLoginEntity */
			CriteriaQuery<CustomerLoginEntity> criteriaQuery = criteriaBuilder.createQuery(CustomerLoginEntity.class);
			
			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<CustomerLoginEntity> root = criteriaQuery.from(CustomerLoginEntity.class);
			
			/* Get the property loginName using root 
			 * equals method can be used of builder for comparing */
			criteriaQuery.where(criteriaBuilder.equal(root.get("customerId"), customerId));
			
			/*
			 * createQuery: is used to Convert the Criteria to executable Query which can be queried
			 * uiqueResult: is used to execute the query and get single entity instead of list of entities
			 *  */
			CustomerLoginEntity customerLoginEntity = session.createQuery(criteriaQuery).uniqueResult();
			
			/* here we are getting values from entity class and setting it to bean class */
			if (customerLoginEntity != null) {
				customerLogin = new CustomerLogin();
				customerLogin.setCustomerLoginId(customerLoginEntity.getCustomerLoginId());
				customerLogin.setCustomerId(customerLoginEntity.getCustomerId());
				customerLogin.setLoginName(customerLoginEntity.getLoginName());
				customerLogin.setLockedStatus(customerLoginEntity.getLockedStatus());
				customerLogin.setPassword(customerLoginEntity.getPassword());
			}
		
		return customerLogin;
	}

	/**
	 * This method is used to change the password of an existing Customer. It
	 * takes CustomerLogin as a parameter, which includes, customerId and
	 * newPassword, it fetches an entity on the basis of customerId, and updates
	 * the password to newPassword received.<br>
	 * It uses CriteriaBuilder to interact with database for fetching the data
	 * 
	 * @param customerLogin
	 * 
	 */
	@Override
	public void changeCustomerPassword(CustomerLogin customerLogin) throws Exception {
		
		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();
					
			/* Create a builder which is used to create criteria. 
			 * This builder contains many methods for arithmetic, logical and other operations */
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			
			/*
			 * Creating the criteria query specifying the required output i.e. CustomerLoginEntity */
			CriteriaQuery<CustomerLoginEntity> criteriaQuery = criteriaBuilder.createQuery(CustomerLoginEntity.class);
			
			/*
			 *From clause is implemented. 
			 *Root is used for selecting the different properties of the class of from clause */
			Root<CustomerLoginEntity> root = criteriaQuery.from(CustomerLoginEntity.class);
			
			/*
			 * Get the property loginName using root 
			 *equals method can be used of builder for comparing */
			criteriaQuery.where(criteriaBuilder.equal(root.get("customerId"), customerLogin.getCustomerId()));
			
			/*
			 * createQuery: is used to Convert the Criteria to executable Query which can be queried
			 * uiqueResult: is used to execute the query and get single entity instead of list of entities
			 *  */
			CustomerLoginEntity customerLoginEntity = session.createQuery(criteriaQuery).uniqueResult();
			
			/*
			 * Where we are setting the new value in entity which is to be updated */
			customerLoginEntity.setPassword(customerLogin.getNewPassword());

			/*
			 * If we want the changes done to an entity which is in manage
			 * state(Application context) to be reflected in the database, then the
			 * transaction must be committed.
			 */

	}

	/**
	 * This method is check weather the loginName is already present in the
	 * database if so return the number of records with similar loginName<br>
	 *
	 * @param loginName
	 * 
	 * @return numberOfRecordWithSameLogin
	 */
	@Override
	public Long checkAvailabilityOfloginName(String loginName) throws Exception {

		Long noOfCustomerWithsameLoginName = null;
		
		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();

			
			
			/* Create a builder which is used to create criteria. 
			 * This builder contains many methods for arithmetic, logical and other operations */
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			
			
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			
			/*
			 *From clause is implemented. 
			 *Root is used for selecting the different properties of the class of from clause */
			Root<CustomerLoginEntity> root = criteriaQuery.from(CustomerLoginEntity.class);

			criteriaQuery.where(criteriaBuilder.like(root.get("loginName"), loginName + "%"));

			criteriaQuery.select(criteriaBuilder.count(root.get("customerLoginId")));

			noOfCustomerWithsameLoginName = session.createQuery(criteriaQuery).uniqueResult();

		return noOfCustomerWithsameLoginName;

	}

	/**
	 * This method is used to persist a new login details for the newly added
	 * Customer with the data from the model object<br>
	 *
	 * it finds the loginName by using entered eame
	 * 
	 * populate all the values to the entity from bean and persist to the
	 * database
	 * 
	 * @param customer
	 * 
	 * @return customerLoginId
	 */
	@Override
	public Integer addNewCustomerLogin(CustomerLogin customerLogin) throws NoSuchAlgorithmException, Exception {

		Integer customerLoginId = null;
		
		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();

			


			CustomerLoginEntity customerLoginEntity = new CustomerLoginEntity();

			customerLoginEntity.setCustomerId(customerLogin.getCustomerId());
			customerLoginEntity.setLockedStatus(customerLogin.getLockedStatus());
			customerLoginEntity.setLoginName(customerLogin.getLoginName());
			customerLoginEntity.setPassword(Hashing.getHashValue(customerLogin.getPassword()));

			
			/*
			 *  here we are adding the customerLoginEntity to CUSTOMER_LOGIN table using save method 
			 * save method will return primary key of that row
			 */
			customerLoginId = (Integer) session.save(customerLoginEntity);
			
			/*
			 * If we want the changes done to an entity which is in manage
			 * state(Application context) to be reflected in the database, then the
			 * transaction must be committed.
			 */
			
		return customerLoginId;

	}
}
