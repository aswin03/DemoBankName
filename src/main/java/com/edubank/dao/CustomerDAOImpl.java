package com.edubank.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edubank.entity.CustomerEntity;
import com.edubank.model.Customer;


/**
 * This Class contains the methods responsible for interacting with the database with
 * respect to Customer module like getCustomer, registerCustomer.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "customerDao")
public class CustomerDAOImpl implements CustomerDAO {

	@Autowired
	SessionFactory sessionFactory;

	/**
	 * This method is used to get a Customer model corresponding to its
	 * customerId<br>
	 * It uses session.get() method to interact with database for fetching the
	 * data
	 * 
	 * @param customerId
	 * 
	 * @return Customer
	 */
	@Override
	public Customer getCustomerByCustomerId(Integer customerId) throws Exception {

		Customer customer = null;
		
		
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
			 * here we are fetching the entity from database on the given
			 * condition
			 **/
			CustomerEntity customerEntity = session.get(CustomerEntity.class,
					customerId);
			
			/* if we receive the entity then
			 * we are creating the bean object
			 * then setting the values from entity
			 * to bean object
			 **/
			if (customerEntity != null) {
				customer = new Customer();

				customer.setCustomerId(customerEntity.getCustomerId());
				customer.setEmailId(customerEntity.getEmailId());
				customer.setName(customerEntity.getName());
				customer.setDateOfBirth(customerEntity.getDateOfBirth());
				customer.setSecurityQuestionId(customerEntity.getSecurityQuestionId());
				customer.setSecurityAnswer(customerEntity.getSecurityAnswer());
			}

		return customer;

	}

	/**
	 * This method is used to add a new Customer with the data from the model
	 * object<br>
	 * It uses session.save() method to save the entity to the database we use
	 * tellerId from http session to set created by field
	 * 
	 * 
	 * @param customer
	 *            , tellerId
	 * 
	 * @return customerId
	 */
	@Override
	public Integer addNewCustomer(Customer customer) throws Exception {

		Integer customerId = null;

		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();
			
			/**
			 * getTransaction Return the resource-level EntityTransaction object.
			 * The EntityTransaction instance may be used serially to begin
			 * and commit multiple transactions
			 * */

			/*
			 * here we are creating the bean object 
			 * then setting the values from entity class
			 * to bean object
			 **/
			CustomerEntity customerEntity = new CustomerEntity();
			customerEntity.setName(customer.getName());
			customerEntity.setDateOfBirth(customer.getDateOfBirth());
			customerEntity.setEmailId(customer.getEmailId());
			customerEntity.setCreatedBy(customer.getCreatedBy());
			customerEntity.setSecurityQuestionId(customer.getSecurityQuestionId());
			customerEntity.setSecurityAnswer(customer.getSecurityAnswer());

			/*
			 * here we are adding the Entity to table using save method save
			 * method will return primary key of that row
			 */
			customerId = (Integer) session.save(customerEntity);

			/*
			 * If we want the changes done to an entity which is in manage
			 * state(Application context) to be reflected in the database, then
			 * the transaction must be committed.
			 */

		return customerId;

	}

	/**
	 * This method is used to get the number customers present with same emailId
	 * It uses criteria to fetch the data from the database
	 * 
	 * @param emailId
	 * 
	 * @return boolean value true if the customer with same emailId present
	 *         false if the customer with same emailId is not present
	 */
	@Override
	public Boolean checkEmailAvailability(String emailId) throws Exception {

		
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
			 * CriteriaBuilder Used to construct criteria queries, compound
			 * selections, expressions, predicates, orderings. here we are
			 * fetching value from database using criteria builder equal method
			 * and count this will return the no of values in the database on
			 * the given condition
			 **/
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<CustomerEntity> root = criteriaQuery.from(CustomerEntity.class);
			
			/* Get the property loginName using root 
			 * equals method can be used of builder for comparing */
			criteriaQuery.where(criteriaBuilder.equal(root.get("emailId"), emailId));

			criteriaQuery.select(criteriaBuilder.count(root.get("customerId")));

			 /* uiqueResult: is used to execute the query and get 
			  * single entity instead of list of entities
			  */
			Long value = session.createQuery(criteriaQuery).uniqueResult();
			
			/*
			 * here according to the fetched result
			 * we are checking it in condition 
			 * and returning the corresponding value
			 **/
			if (value == 0) {
				return false;
			} else {
				return true;
			}
	}

	/**
	 * This method is used to get count of customers
	 * 
	 * @return number of customers
	 */
	@Override
	public Long getNoOfCustomers() throws Exception {

		Long noOfCustomers = null;
		
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
			 * CriteriaBuilder Used to construct criteria queries, compound
			 * selections, expressions, predicates, orderings. here we are
			 * fetching value from database using criteria builder count method
			 * this will return the no of values in the database on
			 * the given condition
			 **/
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<CustomerEntity> root = criteriaQuery.from(CustomerEntity.class);

			criteriaQuery.select(criteriaBuilder.count(root.get("customerId")));

			 /* uiqueResult: is used to execute the query and get 
			  * single entity instead of list of entities
			  */
			noOfCustomers = session.createQuery(criteriaQuery).uniqueResult();

		return noOfCustomers;
	}

	/**
	 * This method is used to get a Customer model corresponding to the given customer 
	 * emailId. <br>
	 * It uses Hibernate criteria to interact with database for fetching the
	 * data
	 * 
	 * @param emailId
	 * 
	 * @return Customer
	 */
	@Override
	public Customer getCustomerByEmailId(String emailId) throws Exception {

		Customer customer = null;
		
		
		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<CustomerEntity> criteriaQuery = criteriaBuilder.createQuery(CustomerEntity.class);

			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<CustomerEntity> root = criteriaQuery.from(CustomerEntity.class);
			
			/* Get the property emailId using root, 
			 * builder's equal method can be used for comparing 
			 */ 
			criteriaQuery.where(criteriaBuilder.equal(root.get("emailId"), emailId.toLowerCase()));


			 /* uniqueResult: is used to execute the query and get 
			  * single entity instead of list of entities
			  */
			CustomerEntity customerEntity = session.createQuery(criteriaQuery).uniqueResult();
			
			/* if we receive the entity then
			 * we are creating the model object
			 * then setting the values from entity
			 * to model object
			 **/
			if (customerEntity != null) {
				customer = new Customer();

				customer.setCustomerId(customerEntity.getCustomerId());
				customer.setEmailId(customerEntity.getEmailId());
				customer.setName(customerEntity.getName());
				customer.setDateOfBirth(customerEntity.getDateOfBirth());
				customer.setSecurityQuestionId(customerEntity.getSecurityQuestionId());
				customer.setSecurityAnswer(customerEntity.getSecurityAnswer());

			}

		return customer;
	}

	@Override
	public List<Customer> getAllCustomersOfTeller(Integer tellerId) throws Exception {
		
		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<CustomerEntity> criteriaQuery = criteriaBuilder.createQuery(CustomerEntity.class);

			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<CustomerEntity> root = criteriaQuery.from(CustomerEntity.class);
			
			/* Get the property emailId using root, 
			 * builder's equal method can be used for comparing 
			 */ 
			criteriaQuery.where(criteriaBuilder.equal(root.get("createdBy"), tellerId));


			 /* uniqueResult: is used to execute the query and get 
			  * single entity instead of list of entities
			  */
			List<CustomerEntity> customerEntities = session.createQuery(criteriaQuery).getResultList();
			
			/* if we receive the entity then
			 * we are creating the model object
			 * then setting the values from entity
			 * to model object
			 **/
			List<Customer> customers = new ArrayList<Customer>();
			for (CustomerEntity customerEntity : customerEntities) {
				Customer customer = new Customer();

				customer.setCustomerId(customerEntity.getCustomerId());
				customer.setEmailId(customerEntity.getEmailId());
				customer.setName(customerEntity.getName());
				customer.setDateOfBirth(customerEntity.getDateOfBirth());
				customer.setSecurityQuestionId(customerEntity.getSecurityQuestionId());
				customer.setSecurityAnswer(customerEntity.getSecurityAnswer());
				customers.add(customer);
			}

		return customers;
	}
}

