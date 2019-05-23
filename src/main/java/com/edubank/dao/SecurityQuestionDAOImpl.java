package com.edubank.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edubank.entity.SecurityQuestionEntity;
import com.edubank.model.SecurityQuestion;

/**
 * This class contains the methods responsible for interacting with the database with
 * respect to security questions functionality
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "securityQuestionDao")
public class SecurityQuestionDAOImpl implements SecurityQuestionDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	/**
	 * This method is used to get all the security questions
	 * 
	 * @return List<SecurityQuestion>
	 */
	@Override
	public List<SecurityQuestion> getAllSecurityQuestions() throws Exception {
		
		List<SecurityQuestion> securityQuestionsList = new ArrayList<>();
		
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
		 * fetching all the security questions from the database.
		 **/
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<SecurityQuestionEntity> criteriaQuery = criteriaBuilder.createQuery(SecurityQuestionEntity.class);
		
		/*
		 * The above criteria returns us the list of entities
		 **/
		criteriaQuery.from(SecurityQuestionEntity.class);
		List<SecurityQuestionEntity> securityQuesitonsListFromDAO = session.createQuery(criteriaQuery).getResultList();
		
		for(SecurityQuestionEntity questionEntity : securityQuesitonsListFromDAO) {
			SecurityQuestion securityQuestion = new SecurityQuestion();
			securityQuestion.setQuestionId(questionEntity.getQuestionId());
			securityQuestion.setQuestion(questionEntity.getQuestion());
			securityQuestionsList.add(securityQuestion);
		}
		
		return securityQuestionsList;
	}
}
