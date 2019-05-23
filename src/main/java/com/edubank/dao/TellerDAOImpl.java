package com.edubank.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edubank.entity.TellerEntity;
import com.edubank.model.Teller;

/**
 * This Interface contains the method responsible for interacting the database with
 * respect to Login module like getLoginOfTeller
 * 
 * @author ETA_JAVA
 *
 */
@Repository("tellerDAO")
public class TellerDAOImpl implements TellerDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	/**
	 * This method is used to get a Teller model corresponding to its
	 * loginName<br>
	 * It uses CriteriaBuilder to interact with database for fetching the data
	 * 
	 * @param loginName
	 * 
	 * @return teller
	 */
	@Override
	public Teller getLoginForTeller(String loginName) throws Exception {

		loginName = loginName.toUpperCase();
		Teller teller = null;

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
			 * here we are fetching the entity class on given condition
			 * condition is given using criteria builder equals method
		
			 * CriteriaBuilder Used to construct criteria queries, compound
			 * selections, expressions, predicates, orderings.
			 **/
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<TellerEntity> criteriaQuery = criteriaBuilder
					.createQuery(TellerEntity.class);

			/* From clause is implemented. 
			 * Root is used for selecting the different properties of the class of from clause */
			Root<TellerEntity> root = criteriaQuery
					.from(TellerEntity.class);

			/* Get the property loginName using root 
			 * equals method can be used of builder for comparing */
			criteriaQuery.where(criteriaBuilder.equal(root.get("loginName"), loginName));

			/*  uiqueResult: is used to execute the query and get 
			  * single entity instead of list of entities
			  */
			TellerEntity tellerEntity = session.createQuery(criteriaQuery)
					.uniqueResult();

			/*
			 * here if we get the entity then we are setting the entity values to bean class
			 * */
			if (tellerEntity != null) {
				teller = new Teller();
				teller.setTellerId(tellerEntity
						.getTellerId());
				teller.setPassword(tellerEntity.getPassword());
				teller.setLoginName(tellerEntity.getLoginName());
			}

			return teller;
	}

}
