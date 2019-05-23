package org.infy.jira;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.Assert;

public class DemoScript {

	WebDriver driver;
	String URL;
	String expectedContext = "Balance";
	
	@Before
	public void setUp() throws Exception {
		
		System.setProperty("webdriver.chrome.driver","C:\\Users\\jeevasuriya.s\\Desktop\\Chrome_2.37.exe");
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void test() throws InterruptedException {
		
		driver= new ChromeDriver();
		
		URL = "http://vqtools122:7979/BankApp/";
		driver.get(URL);
		driver.manage().window().maximize();
	
		driver.findElement(By.id("userId")).sendKeys("admin");
		driver.findElement(By.id("password")).sendKeys("admin");
		driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[2]/form/button")).click();
		
		String actualContext = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/div[1]/span")).getText();
		
		Assert.assertEquals(expectedContext, actualContext);
		
	}

}
