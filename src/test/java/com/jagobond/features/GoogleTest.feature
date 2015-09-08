@google
Feature: FastFT in Origami
  As a user of the FastFT page
  I want an experience to match the existing FastFT functionality
  So that I can prove the utility of Origami


  @smoke
  Scenario Outline: - The FastFT index page passes a basic set of smoke tests when it runs

    Given I am using a <typeOfBrowser> browser
    When I open a browser window of size <width> by <height>
    And I navigate to a Google search page
    Then the google search page elements are displayed

  @chrome
  Examples:
    | typeOfBrowser           | width | height |
    | chrome_latest           | 500   | 800    |
    | chrome_latest           | 640   | 800    |
    | chrome_latest           | 1024  | 800    |
    | chrome_latest           | 1600  | 800    |
    | chrome_latest_minus_one | 500   | 800    |
    | chrome_latest_minus_one | 640   | 800    |
    | chrome_latest_minus_one | 1024  | 800    |
    | chrome_latest_minus_one | 1600  | 800    |

  @firefox
  Examples:
    | typeOfBrowser            | width | height |
    | firefox_latest           | 500   | 800    |
    | firefox_latest           | 640   | 800    |
    | firefox_latest           | 1024  | 800    |
    | firefox_latest           | 1600  | 800    |
    | firefox_latest_minus_one | 500   | 800    |
    | firefox_latest_minus_one | 640   | 800    |
    | firefox_latest_minus_one | 1024  | 800    |
    | firefox_latest_minus_one | 1600  | 800    |