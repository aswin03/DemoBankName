package com.edubank.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edubank.entity.AccountCustomerMappingEntity;
import com.edubank.entity.AccountEntity;
import com.edubank.entity.BranchEntity;
import com.edubank.entity.CustomerEntity;
import com.edubank.model.Account;
import com.edubank.model.AccountCustomerMapping;
import com.edubank.model.AccountCustomerMappingStatus;
import com.edubank.model.Branch;
import com.edubank.model.Customer;
import com.edubank.utility.ApplicationConstants;

/**
 * This is a DAO class contains the methods for persistence logics related to
 * Account module, verifyAccountDetail, getAccountByCustomerId
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "accountDAO")
public class AccountDAOImpl implements AccountDAO {
	
	/** This is a spring auto-wired attribute used to create data base sessions */
	@Autowired
	SessionFactory sessionFactory;

	/**
	 * @see com.edubank.dao.AccountDAO#getBranchDetails(java.lang.Integer)
	 */
	@Override
	public Branch getBranchDetails(Integer branchId) throws Exception{
		Branch branch = null;

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
		 * here we are fetching the branchEntity where it's equal to branchId
		 * */

		BranchEntity branchEntity = session.get(BranchEntity.class, branchId);

		/*
		 * here we are setting values to bean class from entity class only if
		 * the entity class exists
		 **/
		if (branchEntity != null) {
			branch =new Branch();
			branch.setBranchId(branchEntity.getBranchId());
			branch.setBranchName(branchEntity.getBranchName());
			branch.setIfsc(branchEntity.getIfsc());
			branch.setBranchCode(branchEntity.getBranchCode());
		}

		return branch;
	}

	/**
	 * This method is used to fetch the AccountCustomerMapping object
	 * corresponding to customerId
	 * 
	 * @param customerId
	 * 
	 * @return accountCustomerMapping
	 */
	@Override
	public AccountCustomerMapping getAccountCustomerMappingByCustomerId(
			Integer customerId) throws Exception {

		AccountCustomerMapping accountCustomerMapping = null;

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
		 * selections, expressions, predicates, orderings. here we are fetching
		 * value from database using criteria builder equal method this will
		 * return the no of values in the database on the given condition
		 **/
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<AccountCustomerMappingEntity> criteriaQuery = criteriaBuilder
				.createQuery(AccountCustomerMappingEntity.class);

		Root<AccountCustomerMappingEntity> root = criteriaQuery
				.from(AccountCustomerMappingEntity.class);

		/*
		 * here we are fetching the AccountCustomerMappingEntity where it's
		 * equal to customerId
		 **/
		criteriaQuery.where(criteriaBuilder.equal(root.get("customerId"), customerId));

		AccountCustomerMappingEntity accountCustomerMappingEntity = session
				.createQuery(criteriaQuery).uniqueResult();

		/*
		 * here we are setting values to bean class from entity class only if
		 * the entity class exists
		 **/
		if (accountCustomerMappingEntity != null) {
			
			accountCustomerMapping = new AccountCustomerMapping();
			accountCustomerMapping
					.setAccountCustomerMappingId(accountCustomerMappingEntity
							.getAccountCustomerMappingId());
			accountCustomerMapping
					.setAccountNumber(accountCustomerMappingEntity
							.getAccountNumber());
			accountCustomerMapping.setCustomerId(accountCustomerMappingEntity
					.getCustomerId());
			accountCustomerMapping.setMappingStatus(accountCustomerMappingEntity
					.getMappingStatus());
		}

		return accountCustomerMapping;

	}

	/**
	 * This method is used to fetch the account object corresponding to
	 * accountNumber
	 * 
	 * @param accountNumber
	 * 
	 * @return account
	 */
	@Override
	public Account getAccountByAccountNumber(String accountNumber) throws Exception{

		Account account = null;

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
		 * selections, expressions, predicates, orderings. here we are fetching
		 * value from database using criteria builder equal method this will
		 * return the no of values in the database on the given condition
		 **/
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder
				.createQuery(AccountEntity.class);

		Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);

		criteriaQuery.where(criteriaBuilder.equal(root.get("accountNumber"), accountNumber));

		AccountEntity accountEntity = session.createQuery(criteriaQuery)
				.uniqueResult();

		/*
		 * here we are setting values to bean class from entity class only if
		 * the entity class exists
		 **/
		if (accountEntity != null) {
			account = new Account();
			account.setAccountNumber(accountEntity.getAccountNumber());
			account.setBalance(accountEntity.getBalance());
			account.setBranchId(accountEntity.getBranchId());
			account.setAccountStatus(accountEntity.getAccountStatus());
			account.setLockedBalance(accountEntity.getLockedBalance());
		}

		return account;
	}

	/**
	 * This method is used to persist the new account details for the newly
	 * added Customer with the data from the model object<br>
	 *
	 * populate all the values to the entity from bean and persist using save()
	 * method
	 * 
	 * @param account
	 * 
	 * @return accountNumber
	 */
	@Override
	public String addAccount(Account account) throws Exception {

		String accountNumber = null;

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
		 * here we are creating an accountEntity object
		 **/		
		AccountEntity accountEntity = new AccountEntity();

		/*
		 * here we are setting value from bean to entity class
		 **/
		accountEntity.setAccountNumber(getNextAccountNumber());
		accountEntity.setBalance(account.getBalance());
		accountEntity.setBranchId(account.getBranchId());
		accountEntity.setAccountStatus(account.getAccountStatus());
		accountEntity.setLockedBalance(0d);

		/*
		 * here we are adding the Entity to table using save method save method
		 * will return primary key of that row
		 */
		session.save(accountEntity);

		/*
		 * If we want the changes done to an entity which is in manage
		 * state(Application context) to be reflected in the database, then the
		 * transaction must be committed.
		 */

		accountNumber = accountEntity.getAccountNumber();
		
		return accountNumber;
	}

	/**
	 * This is a supporting method for addAccount method<br>
	 * It is used to get the next customer accountNumber
	 * 
	 * @return String
	 */
	private String getNextAccountNumber() {

		String nextAccountNumber = null;

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
		 * selections, expressions, predicates, orderings. here we are fetching
		 * value from database using criteria builder this will return the
		 * values in the database on the given condition
		 **/
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder
				.createQuery(AccountEntity.class);

		Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);

		/*
		 * this will return us a list of AccountEntity sorted in decreasing
		 * order of accountNumber
		 **/
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("accountNumber")));
		List<AccountEntity> entities = session.createQuery(criteriaQuery).getResultList();

		/*
		 * here we are checking if the their are no customer in db then we are
		 * adding the default if their are accounts in db then we are appending
		 * account no with some business logic
		 **/
		String accountNumber;
		if (entities.isEmpty()) {
			accountNumber = ApplicationConstants.FIRST_ACCOUNT_NUMBER;
		} else {
			accountNumber = entities.get(0).getAccountNumber();
		}
		nextAccountNumber = (Long.parseLong(accountNumber) + (int) (Math
				.random() * 100)) + "";

		return nextAccountNumber;
	}

	/**
	 * This method is used to persist a mapping record i.e. customerId and the
	 * account number to be mapped<br>
	 *
	 * populate the entity using parameters and persist it using save() method
	 *
	 * @param customerId
	 *            , accountNumber
	 * @return accountCustomerMappingId
	 */
	@Override
	public Integer addAccountCustomerMapping(Integer customerId,
			String accountNumber) throws Exception {

		Integer mappingId = null;

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
		 * here we are creating the entity class then we are setting the values
		 * from bean class 
		 **/
		AccountCustomerMappingEntity mappingEntity = new AccountCustomerMappingEntity();

		mappingEntity.setAccountNumber(accountNumber);
		mappingEntity.setCustomerId(customerId);
		mappingEntity.setMappingStatus(AccountCustomerMappingStatus.ACTIVE);

		/*
		 * here we are adding the Entity to table using save method save method
		 * will return primary key of that row
		 */
		mappingId = (Integer) session.save(mappingEntity);

		/*
		 * If we want the changes done to an entity which is in manage
		 * state(Application context) to be reflected in the database, then the
		 * transaction must be committed.
		 */

		return mappingId;
	}

	/**
	 * This method is used to credit the amount to the customer account<br>
	 * It adds the amount to existing balance
	 * 
	 * @param accountNumber
	 *            ,amount
	 */
	@Override
	public void creditMoney(String accountNumber, Double amount) throws Exception{


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
		CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder
				.createQuery(AccountEntity.class);

		Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);

		criteriaQuery.where(criteriaBuilder.equal(root.get("accountNumber"), accountNumber));

		AccountEntity accountEntity = session.createQuery(criteriaQuery)
				.uniqueResult();
		Double accountBalance = accountEntity.getBalance();
		accountEntity.setBalance(accountBalance + amount);

		/*
		 * If we want the changes done to an entity which is in manage
		 * state(Application context) to be reflected in the database, then the
		 * transaction must be committed.
		 */

	}

	/**
	 * This method is used to fetch the accountNumber using the mappingId
	 * 
	 * @param mappingId
	 * 
	 * @return accountNumber
	 */
	@Override
	public String getAccountNumberForMappingId(Integer mappingId)  throws Exception{

		String accountNumber = null;

		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();

		AccountCustomerMappingEntity entity = session.get(
				AccountCustomerMappingEntity.class, mappingId);

		accountNumber = entity.getAccountNumber();

		return accountNumber;
	}

	/**
	 * This method is used to debit the money from the given account
	 * 
	 * @param accountNumber, amount
	 *            
	 */
	@Override
	public void debitMoney(String accountNumber, Double amount) throws Exception {


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
		CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder
				.createQuery(AccountEntity.class);

		Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);

		criteriaQuery.where(criteriaBuilder.equal(root.get("accountNumber"), accountNumber));

		AccountEntity accountEntity = session.createQuery(criteriaQuery)
				.uniqueResult();

		Double balance = accountEntity.getBalance();
		accountEntity.setBalance(balance - amount);
		
		/*
		 * If we want the changes done to an entity which is in manage
		 * state(Application context) to be reflected in the database, then the
		 * transaction must be committed.
		 */

	}

	/**
	 * @see com.edubank.dao.AccountDAO#getAccountCustomerMappingByAccountNumber(java.lang.String)
	 */
	@Override
	public AccountCustomerMapping getAccountCustomerMappingByAccountNumber(
			String accountNumber) throws Exception {
		AccountCustomerMapping accountCustomerMapping = null;

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
		CriteriaQuery<AccountCustomerMappingEntity> criteriaQuery = criteriaBuilder
				.createQuery(AccountCustomerMappingEntity.class);

		Root<AccountCustomerMappingEntity> root = criteriaQuery
				.from(AccountCustomerMappingEntity.class);

		criteriaQuery.where(criteriaBuilder.equal(root.get("accountNumber"), accountNumber));

		AccountCustomerMappingEntity accountCustomerMappingEntity = session
				.createQuery(criteriaQuery).uniqueResult();

		if (accountCustomerMappingEntity != null) {
			
			accountCustomerMapping =new AccountCustomerMapping();
			accountCustomerMapping
					.setAccountCustomerMappingId(accountCustomerMappingEntity
							.getAccountCustomerMappingId());
			accountCustomerMapping
					.setAccountNumber(accountCustomerMappingEntity
							.getAccountNumber());
			accountCustomerMapping.setCustomerId(accountCustomerMappingEntity
					.getCustomerId());
			accountCustomerMapping.setMappingStatus(accountCustomerMappingEntity
					.getMappingStatus());
		}

		return accountCustomerMapping;

	}

	/**
	 * @see com.edubank.dao.AccountDAO#creditMoneyToAccount(java.lang.String,
	 *      java.lang.Double)
	 */
	@Override
	public Double creditMoneyToAccount(String accountNumber, Double amount) throws Exception {
		Double updatedBalance = null;

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
		CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder
				.createQuery(AccountEntity.class);

		Root<AccountEntity> rootGet = criteriaQuery.from(AccountEntity.class);

		criteriaQuery.where(criteriaBuilder.equal(rootGet.get("accountNumber"),
				accountNumber));

		AccountEntity accountEntity = session.createQuery(criteriaQuery)
				.uniqueResult();

		Integer rowsUpdated = 0;
		if (accountEntity != null) {
			updatedBalance = accountEntity.getBalance() + amount;
			accountEntity.setBalance(updatedBalance);
			rowsUpdated++;
		}

		/*
		 * rowsUpdated value will remain Zero if account does not exist with the
		 * given account number. If account does not exist null is being
		 * returned
		 */
		if (rowsUpdated == 0)
			return null;

		return updatedBalance;
	}

	/**
	 * This method is used to fetch balance of the given account
	 * 
	 * @param accountNumber
	 * 
	 * @return balance
	 */
	@Override
	public Double getBalanceOfAccountNumber(String accountNumber) throws Exception {

		Double balance = null;

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
		CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);

		Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);

		criteriaQuery.select(root.get("balance"));
		criteriaQuery.where(criteriaBuilder.equal(root.get("accountNumber"), accountNumber));

		balance = session.createQuery(criteriaQuery).uniqueResult();

		return balance;

	}
	
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

			}

		return customer;

	}
}
