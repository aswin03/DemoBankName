package com.edubank.dao;

import com.edubank.model.Transaction;

/**
 * This is a DAO Interface, used to interact with database, contains the methods
 * related to net banking module like payByNetBanking.
 * 
 * @author ETA_JAVA
 *
 */
public interface NetBankingDAO {

	/**
	 * This method is used to get a persist a transaction record and update the
	 * amount in the corresponding account. It uses CriteriaBuilder to interact
	 * 
	 * @param transaction
	 * 
	 * @return transactionId
	 */
	public Long payByNetBanking(Transaction transaction) throws Exception;
}
