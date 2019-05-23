package com.edubank.dao;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edubank.entity.AccountCustomerMappingEntity;
import com.edubank.entity.CustomerEntity;
import com.edubank.entity.DebitCardEntity;
import com.edubank.model.DebitCard;
import com.edubank.utility.ApplicationConstants;
import com.edubank.utility.Hashing;


/**
 * This is a DAO class having methods to perform CRUD operations on debit card
 * table.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "debitCardDAO")
public class DebitCardDAOImpl implements DebitCardDAO
{
	/** This is a spring auto-wired attribute used to create data base sessions */
	@Autowired
	SessionFactory sessionFactory;

	
	/**
	 * @see com.edubank.dao.DebitCardDAO#getDebitCardDetails(java.lang.String)
	 */
	@Override
	public DebitCard getDebitCardDetails(String debitCardNumber) throws Exception
	{

		DebitCard debitCard=null;
		
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
			
			/* Creating the criteria query specifying the required output i.e. DebitCardEntity */
			CriteriaQuery<DebitCardEntity> criteriaQuery = criteriaBuilder
					.createQuery(DebitCardEntity.class);
			
			/*
			 * here we are fetching the entity class on given condition
			 * condition is given using criteria builder equals method
			 **/
			
			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<DebitCardEntity> root = criteriaQuery.from(DebitCardEntity.class);
			
			/* Get the property loginName using root 
			 * equals method can be used of builder for comparing */
			criteriaQuery.where(criteriaBuilder.equal(root.get("debitCardNumber"),
					debitCardNumber));
			
			/*  uiqueResult: is used to execute the query and get 
			 *  single entity instead of list of entities*/
			DebitCardEntity debitCardEntity = session.createQuery(criteriaQuery)
					.uniqueResult();
			
			
			
			if (debitCardEntity == null)
				return null;
			
			AccountCustomerMappingEntity accountCustomerMappingEntity = session
					.get(AccountCustomerMappingEntity.class, debitCardEntity.getAccountCustomerMappingId());
			
			/*
			 * here again we are fetching the entity class on basis of customerId	
			 */
			CustomerEntity customerEntity = session.get(CustomerEntity.class, accountCustomerMappingEntity.getCustomerId());
			
			
			/*
			 * here we are setting values to bean class from entity
			 *that we have fetched from database on given conditions
			 */
			debitCard =new DebitCard();
			
			debitCard.setDebitCardNumber(debitCardEntity.getDebitCardNumber());
			debitCard.setCardHolderName(customerEntity.getName());
			debitCard.setCvv(debitCardEntity.getCvv());
			debitCard.setPin(debitCardEntity.getPin());
			debitCard.setValidFrom(debitCardEntity.getValidFrom());
			debitCard.setValidThru(debitCardEntity.getValidThru());
			debitCard.setDebitCardStatus(debitCardEntity.getDebitCardStatus());
			debitCard.setLockedStatus(debitCardEntity.getLockedStatus());

		return debitCard;
	}

	/**
	 * This method is used the change the debit card pin. it fetches the debit
	 * card details by the debit card number and updates the pin.
	 * 
	 * @param debitCard
	 */
	@Override
	public void changeDebitCardPin(DebitCard debitCard) throws Exception {

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
			
			CriteriaQuery<DebitCardEntity> criteriaQuery = criteriaBuilder
					.createQuery(DebitCardEntity.class);
			
			/*
			 * here we are fetching the entity class on given condition
			 * condition is given using criteria builder equals method
			 **/
			Root<DebitCardEntity> root = criteriaQuery.from(DebitCardEntity.class);
			
			/* Get the property loginName using root 
			 * equals method can be used of builder for comparing */
			criteriaQuery.where(criteriaBuilder.equal(root.get("debitCardNumber"),
					debitCard.getDebitCardNumber()));
			
			/*
			 * createQuery: is used to Convert the Criteria to executable Query which can be queried
			 * uiqueResult: is used to execute the query and get single entity instead of list of entities
			 **/
			DebitCardEntity debitCardEntity = session.createQuery(criteriaQuery)
					.uniqueResult();
			if(debitCardEntity!=null)
			{
				debitCardEntity.setPin(debitCard.getNewPin());
			}
			
			
		
	}

	/**
	 * This method takes accountCustomerMappingId and returns the corresponding
	 * Debit Card Details
	 * 
	 * @param accountCustomerMappingId
	 * 
	 * @return debitCard
	 */
	@Override
	public DebitCard getDebitCardDetailsByAccountCustomerMappingId(
			Integer accountCustomerMappingId) throws Exception {
		
		DebitCard debitCard = null;

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
			
			/* Creating the criteria query specifying the required output i.e. debitCardEntity */
			CriteriaQuery<DebitCardEntity> criteriaQuery = criteriaBuilder
					.createQuery(DebitCardEntity.class);
			
			/*
			 * here we are fetching the entity class on given condition
			 * condition is given using criteria builder equals method
			 **/
			Root<DebitCardEntity> root = criteriaQuery.from(DebitCardEntity.class);
			
			/* Get the property loginName using root 
			 * equals method can be used of builder for comparing */
			criteriaQuery.where(criteriaBuilder.equal(root.get("accountCustomerMappingId"),
					accountCustomerMappingId));
			
			/*
			 * createQuery: is used to Convert the Criteria to executable Query which can be queried
			 * uiqueResult: is used to execute the query and get single entity instead of list of entities
			 **/
			DebitCardEntity debitCardEntity = session.createQuery(criteriaQuery)
					.uniqueResult();
			
			/*
			 * here we are setting values to bean class from entity class only if
			 * the entity class exists
			 **/
			if(debitCardEntity!=null)
			{	
				
				debitCard = new DebitCard();
				
				debitCard.setDebitCardNumber(debitCardEntity.getDebitCardNumber());
				debitCard.setCvv(debitCardEntity.getCvv());
				debitCard.setPin(debitCardEntity.getPin());
				debitCard.setValidFrom(debitCardEntity.getValidFrom());
				debitCard.setValidThru(debitCardEntity.getValidThru());
				debitCard.setDebitCardStatus(debitCardEntity.getDebitCardStatus());
				debitCard.setLockedStatus(debitCardEntity.getLockedStatus());
			}
		
		return debitCard;
	}
	
	/**
	 * This method returns the maximum of card numbers inserted if no card
	 * number inserted send a default one
	 *
	 * @return debitCardNumber
	 */
	@Override
	public String getLastCardNumber() throws Exception {

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
			
			/* Creating the criteria query specifying the required output */
			CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);

			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<DebitCardEntity> root = criteriaQuery.from(DebitCardEntity.class);
			
			/*
			 * here we are fetching the entity class on given condition
			 * condition is given using criteria builder 
			 *	in this we are fetching values in decreasing order of debitCardNumber
			 **/
			criteriaQuery.select(root.get("debitCardNumber"));

			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("debitCardNumber")));

			/*
			 *  list: is used to execute the query and get list of entity instead single entitiy
			 **/
			List<String> cardNumbers = session.createQuery(criteriaQuery).getResultList();

			if (cardNumbers.isEmpty()) {
				return ApplicationConstants.FIRST_DEBIT_CARD_NUMBER;
			} else {
				return cardNumbers.get(0);
			}
		
	}

	/**
	 * This method is used to add a debit card record for the newly added
	 * customer
	 *
	 * populate the entity from bean and persist using save()
	 *
	 * @param mappingId,
	 *            tellerId
	 */
	@Override
	public void addDebitCard(DebitCard debitCard, Integer tellerId)
			throws NoSuchAlgorithmException, Exception {

		
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
			 * here we are creating an object for entity class
			 **/
			DebitCardEntity cardEntity = new DebitCardEntity();
			
			/*
			 * here we are setting values from bean class to entity
			 **/
			cardEntity.setAccountCustomerMappingId(debitCard.getAccountCustomerMappingId());
			cardEntity.setCvv(Hashing.getHashValue(debitCard.getCvv()));
			cardEntity.setDebitCardNumber(debitCard.getDebitCardNumber());
			cardEntity.setDebitCardStatus(debitCard.getDebitCardStatus());
			cardEntity.setLockedStatus(debitCard.getLockedStatus());
			cardEntity.setPin(Hashing.getHashValue(debitCard.getPin()));
			cardEntity.setValidFrom(debitCard.getValidFrom());
			cardEntity.setValidThru(debitCard.getValidThru());
			cardEntity.setCreatedBy(tellerId);

			/*
			 * here we are adding the Entity to  table using save method 
			 * save method will return primary key of that row
			 */
			session.save(cardEntity);		

	}
	
	/**
	 * This method is used to get the pin of the debit card and
	 * customer account mappingId
	 * 
	 * @param debitCardNumber
	 * 
	 * @return pinAndMappingId as an object
	 */
	@Override
	public Object[] getPinAndMappingIdOfdebitCard(String debitCardNumber) throws Exception {
		Object[] pinAndMappingId = null;
		
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
			CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
			
			/*
			 * here we are fetching the entity class on given condition
			 * condition is given using criteria builder equals method
			 **/
			Root<DebitCardEntity> root = criteriaQuery.from(DebitCardEntity.class);

			criteriaQuery.multiselect(root.get("pin"),root.get("accountCustomerMappingId"));

			criteriaQuery.where(criteriaBuilder.equal(root.get("debitCardNumber"),debitCardNumber));

			pinAndMappingId = session.createQuery(criteriaQuery).uniqueResult();
			
		return pinAndMappingId;
	}

}
