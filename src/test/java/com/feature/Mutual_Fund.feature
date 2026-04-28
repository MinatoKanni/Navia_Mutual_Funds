Feature: Verify Mutual Fund Functionality

Scenario: Login Navia
    Given User Navigate to Navia

Scenario: User Check The Mutual Fund -  UPI Payment - functionality

    When Navigate to home page
    And User MouseOver Dashboard and Click Mutual Funds
    And User Click Explore
    And User Search "Navi Flexi Cap Fund Direct Plan Growth" in Serach Box and enter
    And User Click One Time , enter amount "100" and click pay now
    And User Click enter UPI ID "6374837965@ptsbi" and Click Pay Via UPI
    #And User Click Pay via Netbanking and verify
