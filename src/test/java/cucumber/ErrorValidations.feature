
@tag
Feature: Error validation
  I want to use this template for my feature file


  @ErrorValidation
  Scenario Outline: Title of your scenario outline
    Given I landed on Ecommerce Page
    When Logged in with username <name> and password <password>
    Then "Warning: No match for E-Mail Address and/or Password." message is displayed

     Examples: 
      | name  							|  password		    |
      | gialinh01@gmail.com |  0165610766a    |
      | gialinh02@gmail.com |  0165610766a    | 
