package com.edubank.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edubank.entity.CustomerEntity;
import com.edubank.entity.CustomerLoginEntity;


/**
 * This is a DAO class implementing ForgotPasswordDAO having methods to perform CRUD operations on CUSTOMER and CUSTOMER_LOGIN tables 
 * for customer forgot/reset password requests
 *
 * @author ETA_JAVA
 * 
 */
@Repository(value="forgotPasswordDAO")
public class ForgotPasswordDAOImpl implements ForgotPasswordDAO
{
	/** This is a spring auto-wired attribute used to create data base sessions*/
	@Autowired
	SessionFactory sessionFactory;
		
	
	/**
	 * @see com.edubank.dao.ForgotPasswordDAO#authenticateEmailId(java.lang.String)
	 */
	@Override
	public String authenticateEmailId(String emailId) throws Exception
	{
		
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
			CriteriaBuilder criteriaBuilder =session.getCriteriaBuilder();
			
			/* Creating the criteria query specifying the required output i.e. CustomerEntity */
			CriteriaQuery<CustomerEntity> criteriaQuery = criteriaBuilder.createQuery(CustomerEntity.class);
			
			/*
			 * here we are fetching the entity class on given condition
			 * condition is given using criteria builder equal method
			 **/
			Root<CustomerEntity> root = criteriaQuery.from(CustomerEntity.class);
			criteriaQuery.where(criteriaBuilder.equal(root.get("emailId"),emailId.toLowerCase()));		
			
			/*
			 * creating query on the above criteria is returning us list of entities
			 **/
			List<CustomerEntity> listOfUserEntities = session.createQuery(criteriaQuery).getResultList();
			
			if(listOfUserEntities!=null && !listOfUserEntities.isEmpty())
				return "Found";
			else
				return "Not Found";
	}
	
	/**
	 * @see com.edubank.dao.ForgotPasswordDAO#resetPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public void resetPassword(String emailId, String hashedPassword)  throws Exception
	{
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
			CriteriaBuilder criteriaBuilder =session.getCriteriaBuilder();
			
			/* Creating the criteria query specifying the required output i.e. CustomerEntity */
			CriteriaQuery<CustomerEntity> criteriaQuery = criteriaBuilder.createQuery(CustomerEntity.class);
			
			/*
			 * here we are fetching the entity class on given condition
			 * condition is given using criteria builder equal method
			 **/
			Root<CustomerEntity> root = criteriaQuery.from(CustomerEntity.class);
			criteriaQuery.where(criteriaBuilder.equal(root.get("emailId"),emailId.toLowerCase()));		
			
			/*
			 * createQuery: is used to Convert the Criteria to executable Query which can be queried
			 * uniqueResult: is used to execute the query and get single entity instead of list of entities
			 * */
			CustomerEntity customerEntity = session.createQuery(criteriaQuery).uniqueResult();
			
			CriteriaBuilder criteriaBuilder2 = session.getCriteriaBuilder();
			CriteriaQuery<CustomerLoginEntity> criteriaQuery2 = criteriaBuilder2.createQuery(CustomerLoginEntity.class);

			Root<CustomerLoginEntity> root2 = criteriaQuery2.from(CustomerLoginEntity.class);

			criteriaQuery2.where(criteriaBuilder2.equal(root2.get("customerId"), customerEntity.getCustomerId()));

			CustomerLoginEntity customerLoginEntity = session.createQuery(criteriaQuery2).uniqueResult();
			customerLoginEntity.setPassword(hashedPassword);
			
	}
	
}
