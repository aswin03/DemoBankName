package com.edubank.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edubank.entity.TransactionEntity;
import com.edubank.model.Transaction;


/**
 * This is a DAO class having methods to perform CRUD operations on transaction
 * table.
 * 
 * @author ETA_JAVA
 *
 */
@Repository("transactionDAO")
public class TransactionDAOImpl implements TransactionDAO {

	@Autowired
	SessionFactory sessionFactory;

	/**
	 * This method is used to persist the transaction
	 * 
	 * @param transaction, createdBy
	 *            
	 * @return transactionId
	 */
	@Override
	public Long addTransaction(Transaction transaction, String createdBy) throws Exception {

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
			

			/*
			 * here we are creating an entity object  and
			 * setting the values to that entity object from model object
			 **/
			TransactionEntity transactionEntity = new TransactionEntity();
			transactionEntity.setAccountNumber(transaction.getAccountNumber());
			transactionEntity.setAmount(transaction.getAmount());
			transactionEntity.setCreatedBy(createdBy);
			transactionEntity.setInfo(transaction.getInfo());
			transactionEntity.setRemarks(transaction.getRemarks());
			transactionEntity.setTransactionMode(transaction
					.getTransactionMode());
			transactionEntity.setType(transaction.getType());

			/*
			 * here we are adding the Entity to table using save method save
			 * method will return primary key of that row
			 */
			transactionId = (Long) session.save(transactionEntity);

	
		return transactionId;
	}

	
	/**
	 * @see com.edubank.dao.TransactionDAO#getAccountTransactionsForDateRange
	 * (java.lang.String, java.time.LocalDate, java.time.LocalDate)
	 */
	@Override
	public List<Transaction> getAccountTransactionsForDateRange(
			String accountNumber, LocalDate fromDate, LocalDate toDate) throws Exception {
		List<Transaction> transactionsList = null;
		
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
			 * fetching value from database using criteria builder with various methods
			 * based on the different given condition
			 **/
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<TransactionEntity> criteriaQuery = criteriaBuilder
					.createQuery(TransactionEntity.class);

			 /*
			  * Root is used for selecting the different properties of the class of from clause
			  **/
			Root<TransactionEntity> root = criteriaQuery
					.from(TransactionEntity.class);

			/*
			 * here we are creating LocaldateTime object
			 * to pass it to criteria so to get transaction 
			 * between the given dates
			 **/ 
			LocalDateTime fromDateTime = LocalDateTime.of(fromDate,
					LocalTime.of(0, 0, 0, 0));
			LocalDateTime toDateTime = LocalDateTime
					.of(toDate, LocalTime.now());

			Predicate p1 = criteriaBuilder.equal(root.get("accountNumber"),
					accountNumber);
			Predicate p2 = criteriaBuilder.between(root.get("transactionDateTime"),
					fromDateTime, toDateTime);

			criteriaQuery.where(p1, p2);
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("transactionDateTime")));
			
			/*
			 * the above criteria return us the list of entities
			 **/
			List<TransactionEntity> transactionsListFromDAO = session
					.createQuery(criteriaQuery).getResultList();
			
			/*
			 * here if the list of entities length is more than 1 then we are
			 * creating a bean object using getContext and setting the values
			 * into bean object from entities that we received from database
			 * to iterate over the entity list we are using for loop
			 * then after setting the values to bean object we are adding 
			 * the values to the transaction list  
			 */
			if (!transactionsListFromDAO.isEmpty()) {
				transactionsList = new ArrayList<Transaction>();

				for (TransactionEntity transactionEnt : transactionsListFromDAO) {
					Transaction tran = new Transaction();
					tran.setTransactionId(transactionEnt.getTransactionId());
					tran.setTransactionDateTime(transactionEnt
							.getTransactionDateTime());
					tran.setType(transactionEnt.getType());
					tran.setInfo(transactionEnt.getInfo());
					tran.setAmount(transactionEnt.getAmount());

					transactionsList.add(tran);
				}
			}

		return transactionsList;
	}
}
