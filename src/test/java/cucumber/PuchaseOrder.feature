
@tag
Feature: Purchase the order from Ecommerce Website
  I want to use this template for my feature file

	Background:
	Given I landed on Ecommerce Page

  @Regression
  Scenario Outline: Positive Test of Submitting the order
  
    Given Logged in with username <name> and password <password>
    When I add products to Cart with the following details:
    	| productName 		|
    	|	<productName>   |
    	
    And I go to edit cart page 
    And remove out of stock products
		And update quantity product
			| quantity   |
		  | <quantity> |
		And I go to checkout page to fill address information:
      | firstName   | lastName   | address   | city   | postCode   | country   | region   | comments   | telephone   |
      | <firstName> | <lastName> | <address> | <city> | <postCode> | <country> | <region> | <comments> | <telephone> |
    And I go to confirm page to check address information and submit the order
    	| firstName   | lastName   | address   | city   | postCode   | country   | region   |
      | <firstName> | <lastName> | <address> | <city> | <postCode> | <country> | <region> |
    Then "Your order has been placed!" message is displayed on ConfirmOrderPage

    Examples: 
      | name  								|  password		  |	productName                  |quantity| firstName| lastName| address      |city  | postCode | country | region | comments   																					| telephone  |
      | gialinh2025@gmail.com |  12345        | iPod Nano,MacBook,iPod Touch |1,2,1	  | Linh	   | Nguyen  | 15 Bay Street|Chiba | 17982    |	Japan		| Osaka  | They are useful products and affordable for everyone.| 0907065927 |
