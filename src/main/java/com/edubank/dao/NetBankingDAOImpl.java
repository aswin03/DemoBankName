package com.edubank.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edubank.entity.AccountEntity;
import com.edubank.entity.TransactionEntity;
import com.edubank.model.Transaction;

/**
 * This is a DAO class, used to interact with database, contains the methods
 * related to net banking module like payByNetBanking.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "netBankingDAO")
public class NetBankingDAOImpl implements NetBankingDAO {

	@Autowired
	SessionFactory sessionFactory;

	/**
	 * This method is used to get a persist a transaction record and update the
	 * amount in the corresponding account. It uses CriteriaBuilder to interact
	 * 
	 * @param transaction
	 * 
	 * @return transactionId
	 */
	@Override
	public Long payByNetBanking(Transaction transaction) throws Exception {

		Long transactionId = null;
		
		/*
		 * This is one of the way to get the session from sessionFactory,
		 * getCurrentSession() returns a session bound to a context - you don't
		 * need to close this. It creates a new Session if not exists, else use
		 * same session which is in current spring context. You do not need to
		 * flush and close session objects, it will be automatically taken care
		 * by spring internally.
		 */
		Session session = sessionFactory.getCurrentSession();

			
			
			TransactionEntity entity = new TransactionEntity();
			
			/*
			 * here we are setting values from bean class to entity class
			 **/
			entity.setAccountNumber(transaction.getAccountNumber());
			entity.setAmount(transaction.getAmount());
			entity.setCreatedBy("AmigoWallet");
			entity.setInfo(transaction.getInfo());
			entity.setRemarks(transaction.getRemarks());
			entity.setTransactionMode(transaction.getTransactionMode());
			entity.setType(transaction.getType());
			
			/*
			 * here we are adding the Entity to  table using save method 
			 * save method will return primary key of that row
			 */
			transactionId = (Long) session.save(entity);
			
			/* Create a builder which is used to create criteria. 
			 * This builder contains many methods for arithmetic, logical and other operations */
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			
			/* Creating the criteria query specifying the required output i.e. AccountEntity */
			CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder
					.createQuery(AccountEntity.class);

			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);

			/* Get the property loginName using root 
			 * equals method can be used of builder for comparing */
			criteriaQuery.where(criteriaBuilder.equal(root.get("accountNumber"),
					transaction.getAccountNumber()));

			 /* uiqueResult: is used to execute the query and get 
			  * single entity instead of list of entities
			  */
			AccountEntity accountEntity = session.createQuery(criteriaQuery)
					.uniqueResult();

			accountEntity.setBalance(accountEntity.getBalance()
					- transaction.getAmount());
			
			

		return transactionId;
	}

}
