package ojosanco.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import ojosanco.testcomponents.BaseTest;

public class ErrorLoginTest extends BaseTest {
	String filePath = "jdbc:mysql://localhost:3306/usertest";
	String root = "root";
	String password = "01656107662aA&";
	String query = "select userName, password  from users;";
	@Test(dataProvider = "getData", enabled = true)
	public void loginErrorValidation(HashMap<String, String> input) {
		loginPage.loginApplication(input.get("userName"), input.get("password"));
		Assert.assertEquals(loginPage.getErrorMessage(), "Warning: No match for E-Mail Address and/or Password.");
	}
	@Test(dataProvider = "getData", enabled = false, retryAnalyzer = ojosanco.testcomponents.Retry.class)
	public void confirmErrorForgotPassword(HashMap<String, String> input) {
		loginPage.forgotPasword(input.get("userName"));
		Assert.assertEquals(loginPage.getErrorMessage(), "Warning: The E-Mail Address was not found in our records, please try again!");
	}
	@DataProvider
	public Object[][] getData() throws IOException, SQLException {
		List<HashMap<String, String>> data = getDatabaseDataMap(filePath,root,password,query);
		return new Object[][]{{data.get(2)}};
	} 
}
