package com.edubank.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.edubank.model.Account;
import com.edubank.model.Customer;
import com.edubank.model.Transaction;
import com.edubank.model.TransactionType;
import com.edubank.service.AccountService;
import com.edubank.service.CustomerService;
import com.edubank.service.TransactionService;
import com.edubank.utility.ContextFactory;


/**
 * The {@code AccountAPI} class has methods to handle the requests related to
 * Accounts of Customers
 * 
 * @author ETA_JAVA
 */
@CrossOrigin
@RestController
@RequestMapping("AccountAPI")
public class AccountAPI 
{
	
	/**
	 * The attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;

	static Logger logger = LogManager.getLogger(AccountAPI.class);
	
	/**
	 * This method is a web service API method used to verify account details
	 * coming in the HTTP request. This method is exposed as a web service to
	 * make it available for other applications to consume.
	 * 
	 * @param account
	 * 
	 * @return account verification status as ResponseEntity<String>
	 */
	@RequestMapping(value = { "accountVerification" }, method = RequestMethod.POST)
	public ResponseEntity<String> verifyAccountDetails(
			@RequestBody Account account) {
		logger.info("ACCOUNT VERIFICATION REQUEST WITH ACCOUNT NUMBER "+account.getAccountNumber()+", AND ACCOUNT HOLDER NAME : "+account.getAccountHolderName());
		String status = null;
		try {
			/*
			 * getContext() method of ContextFactory class parses the
			 * SpringConfig class and using @ComponentScan,
			 * 
			 * it scans the packages/classes for which the base package provided
			 * is 'com.edubank'
			 * 
			 * please see (ctrl + click) SpringConfig for more detail
			 * 
			 * then it returns the object of ApplicationContext
			 * 
			 * ApplicationContext holds the objects created by spring framework
			 * 
			 * getBean("accountService") method of ApplicationContext returns
			 * the object of AccountServiceImpl as @Service("accountService") is
			 * specified in AccountServiceImpl
			 * 
			 * The returned object of AccountServiceImpl is parsed and stored in
			 * AccountService type variable
			 */
			AccountService accountVerificationService = (AccountService) ContextFactory.getContext().getBean("accountService");

			/*
			 * The following call to AccountService method is to verify account
			 * number and IFSC, which returns the customerId corresponding to
			 * the account number and IFSC sent
			 */
			Integer customerId = accountVerificationService
					.verifyAccountNumberAndIfsc(account);
			
			/*
			 * getBean("customerService") method of ApplicationContext returns
			 * the object of AccountServiceImpl as @Service("accountService") is
			 * specified in AccountServiceImpl
			 * 
			 * The returned object of CustomerServiceImpl is stored in
			 * CustomerService type variable
			 */
			CustomerService customerService = (CustomerService) ContextFactory
					.getContext().getBean("customerService");
			
			/*
			 * The following code returns the object of Customer corresponding
			 * to the customerId sent
			 */
			Customer customer = customerService
					.getCustomerByCustomerId(customerId);

			/*
			 * The following call to AccountService method is to verify account
			 * holder name whether the name received in this method is matching
			 * the name in database
			 */
			accountVerificationService.verifyAccountHolderName(
					account.getAccountHolderName(), customer.getName());


			logger.info("ACCOUNT VERIFIFIED SUCCESSFULLY WITH ACCOUNT NUMBER "+account.getAccountNumber()+", AND ACCOUNT HOLDER NAME : "+account.getAccountHolderName());

			/* the following code fetches the message from
			 * configuration.properties file using environment object
			 */
			status = environment
					.getProperty("AccountController.ACCOUNT_VERIFICATION_SUCCESS");
			
			/*
			 * ResponseEntity is created and returned using the message fetched
			 * in previous step with HttpStatus as OK
			 */
			return new ResponseEntity<String>(status, HttpStatus.OK);
			
		} catch (Exception e) {
			/*
			 * the following code is executed if any exception occurs during
			 * execution of the above code,
			 * 
			 * It fetches the message from configuration.properties file using
			 * environment object corresponding to the exception message
			 */
			status = environment.getProperty(e.getMessage());
			

			logger.info("ACCOUNT VERIFICATION FAILED...: "+e.getMessage());

			/* ResponseEntity is created and returned using the message fetched
			 * in previous step with HttpStatus as BAD_REQUEST
			 */
			return new ResponseEntity<String>(status, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is a web service API method used to credit money to the given
	 * account number. It receives the values from HTTP request and makes a call
	 * to corresponding service class method.
	 * 
	 * In the annotation given for this method header, "creditMoney/{amount:.+}" the notation "amount:.+"
	 * indicates that decimal point numbers can also be received in the request.
	 * 
	 * @param account
	 * @param amount
	 * 
	 * @return credit money status as ResponseEntity<String>
	 */
	@RequestMapping(value = { "creditMoney/{amount:.+}" }, method = RequestMethod.POST)
	public ResponseEntity<String> creditMoneyToAccount(
			@RequestBody Account account, @PathVariable("amount") Double amount) {
		logger.info("CREDIT MONEY REQUEST FOR ACCOUNT NUMBER : "+account.getAccountNumber()+", AND AMOUNT : "+amount);
		String status = null;
		try {
			/*
			 * getContext() method of ContextFactory class parses the
			 * SpringConfig class and using @ComponentScan,
			 * 
			 * it scans the packages/classes for which the base package provided
			 * is 'com.edubank'
			 * 
			 * please see (ctrl + click) SpringConfig for more detail
			 * 
			 * then it returns the object of ApplicationContext
			 * 
			 * ApplicationContext holds the objects created by spring framework
			 * 
			 * getBean("accountService") method of ApplicationContext returns
			 * the object of AccountServiceImpl as @Service("accountService") is
			 * specified in AccountServiceImpl
			 * 
			 * The returned object of AccountServiceImpl is parsed and stored in
			 * AccountService type variable
			 */
			AccountService accountVerificationService = (AccountService) ContextFactory
					.getContext().getBean("accountService");
			
			/*
			 * this following method call is used to credits the given amount
			 * corresponding to the given account number
			 */
			accountVerificationService.creditMoneyToAccount(
					account.getAccountNumber(), amount);

			/*
			 * getBean("transactionService") method of ApplicationContext returns
			 * the object of TransactionServiceImpl as @Service("transactionService") is
			 * specified in TransactionServiceImpl
			 * 
			 * The returned object of TransactionServiceImpl is parsed and stored in
			 * TransactionService type variable
			 */
			TransactionService transactionService = (TransactionService) ContextFactory
					.getContext().getBean("transactionService");
			
			
			Transaction transaction = new Transaction();

			transaction.setAccountNumber(account.getAccountNumber());
			transaction.setAmount(amount);
			transaction.setType(TransactionType.CREDIT);
			transaction
					.setInfo("From:- AmigoWallet To:- Account, Reason:- transfered");
			transaction.setTransactionMode("AmigoWallet");

			transactionService.addTransaction(transaction, "AmigoWallet");

			/*
			 * the following code fetches the message from
			 * configuration.properties file using environment object
			 */
			status = environment
					.getProperty("AccountController.CREDIT_MONEY_SUCCESS");

			logger.info("MONEY CREDITED SUCCESSFULLY TO ACCOUNT NUMBER : "+account.getAccountNumber()+", WITH AMOUNT : "+amount);

			/* ResponseEntity is created and returned using the message fetched
			 * in previous step with HttpStatus as OK
			 */
			return new ResponseEntity<String>(status, HttpStatus.OK);
			
		} catch (Exception e) 
		{
			/*
			 * the following code is executed if any exception occurs during
			 * execution of the above code,
			 * 
			 * It fetches the message from configuration.properties file using
			 * environment object, corresponding to the exception message
			 */
			status = environment.getProperty(e.getMessage());
			
			/*
			 * ResponseEntity is created and returned using the message fetched
			 * in previous step with HttpStatus as BAD_REQUEST
			 *
			 */
			return new ResponseEntity<String>(status, HttpStatus.BAD_REQUEST);
		}
		
	}
}
