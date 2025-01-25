package ojosanco.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ojosanco.testcomponents.BaseTest;

public class LoginTest extends BaseTest{
	String filePath = "jdbc:mysql://localhost:3306/usertest";
	String root = "root";
	String password = "01656107662aA&";
	String query = "select userName, password  from users;";
	@Test(dataProvider = "getData",enabled = true)
	public void testLoginWithValidCredentials(HashMap<String, String> input){
		loginPage.loginApplication(input.get("userName"), input.get("password"));
	}
	
	@Test(dataProvider = "getData",enabled = false)
	public void testLoginWithForgotPassword(HashMap<String, String> input){
		loginPage.forgotPasword(input.get("userName"));
		Assert.assertEquals(loginPage.getConfirmForgotPasswordMessege(), "An email with a confirmation link has been sent your email address.");
	}
	@DataProvider
	public Object[][] getData() throws IOException, SQLException {
		List<HashMap<String, String>> data = getDatabaseDataMap(filePath,root,password,query);
		return new Object[][]{{data.get(0)}};
	} 
}
