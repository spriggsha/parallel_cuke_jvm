@google
Feature: Google Search Elements
  As a user of the Google search page
  I want to be certain every search element is displayed
  So that I can be certain that basic search functionality is possible


  @smoke
  Scenario Outline: - The Google search page elements are displayed

    Given I am using a <typeOfBrowser> browser
    When I open a browser window of size <width> by <height>
    And I navigate to a Google search page
    Then the google search page elements are displayed

  @chrome
  Examples:
    | typeOfBrowser | width | height |
    | chrome        | 500   | 800    |
    | chrome        | 640   | 800    |
    | chrome        | 1024  | 800    |
    | chrome        | 1600  | 800    |

  @firefox
  Examples:
    | typeOfBrowser | width | height |
    | firefox       | 500   | 800    |
    | firefox       | 640   | 800    |
    | firefox       | 1024  | 800    |
    | firefox       | 1600  | 800    |

  @ie11
  Examples:
    | typeOfBrowser | width | height |
    | ie11          | 500   | 800    |
    | ie11          | 640   | 800    |
    | ie11          | 1024  | 800    |
    | ie11          | 1600  | 800    |