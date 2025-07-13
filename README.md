## I. Calculate Aggregated Revenue

**Problem Statement:**

You are building a Restaurant Management System. As a part of this system, you need to build a functionality using which users with billing privileges can generate aggregated revenue report for the restaurant.

**Requirements:**

<ol>
  <li>The revenue made by the restaurant is stored in the database in daily\_revenue table.
  <ul>
    <li>This table has aggregated data for each day.</li>
    <li>This table contains 4 columns:</li>
    <ul>
      <li>id (primary key)</li>
      <li>date (date on which the revenue was generated)</li>
      <li>revenueFromFoodSales (revenue generated from food sales)</li>
      <li>totalGst (total GST collected)</li>
      <li>totalServiceCharge (total service charge collected)</li>
    </ul>
  </ul>
  <li>Our functionality should support 4 types of queries:</li>
  <ul>
    <li>Get revenue for current month</li>
    <li>Get revenue for current financial year</li>
    <li>Get revenue for previous month</li>
    <li>Get revenue for previous financial year</li>
  </ul>
  <li>The request for calculating revenue will contain user id and query type.</li>
  <li>Depending upon the type of query the functionality should aggregate the relevant revenue data and return the response.</li>
  <li>This functionality will only available to users with billing privileges.</li>
</ol>

**Instructions:**
  <ul>
    <li>Refer the calculateRevenue method inside RevenueController class.</li>
    <li>Refer the CalculateRevenueRequestDto and CalculateRevenueResponseDto for understanding the expected input and output to the functionality.</li>
    <li>Refer the models package to understand the models.</li>
    <li>Implement the RevenueService, UserRepository and DailyRevenueRepository interfaces to achieve the above requirements.</li>
    <li>We need in memory database implementation for this assignment.</li>
    <li>Refer the TestRevenueController class to understand the test cases that will be used to evaluate your solution.</li>
    <li>Do not modify existing methods and their parameters for interfaces, feel free to add more methods if required.</li>
  </ul>

## II. Generate Bill

**Problem Statement:**

You are building a Restaurant Management System. As a part of this system, you need to build a functionality using which customers can generate a bill for their orders.

**Requirements:**
<ol>
  <li>Generally a customer places multiple orders before requesting for the bill. Hence, our system must be able to track orders placed by a customer.</li>
  <ul>
    <li>We should have an entity called as CustomerSession in our system which will help us track the orders placed by a customer.</li>
    <li>Once the customer places their 1st order, we should create a CustomerSession for them with status as ACTIVE.</li>
    <li>All the subsequent orders placed by the customer should be associated with the CustomerSession created for them.</li>
    <li>Once the customer requests for the bill, we should mark the CustomerSession as ENDED.</li>
  </ul>
  <li>The request for generating the bill will contain just the customer id.</li>
  <li>This functionality should aggregate the items ordered by the customer and calculate the total amount to be paid by the customer.</li>
  <li>This functionality should also calculate GST and service charge on the total amount.</li>
  <li>GST will be 5% of the total food cost and service charge will be 10% of the total food cost.</li>
  <li>Return the bill details in the response.</li>
</ol>

**Instructions:**
<ul>
  <li>Refer the generateBill method inside OrderController class.</li>
  <li>Refer the GenerateBillRequestDto and GenerateBillResponseDto for understanding the expected input and output to the functionality.</li>
  <li>Refer the models package to understand the models.</li>
  <li>Implement the OrderService, CustomerSessionRepository and OrderRepository interfaces to achieve the above requirements.</li>
  <li>We need in memory database implementation for this assignment.</li>
  <li>Refer the TestOrderController class to understand the test cases that will be used to evaluate your solution.</li>
  <li>Do not modify existing methods and their parameters for interfaces, feel free to add more methods if required.</li>
</ul>

## III. Track Orders

**Problem Statement:**

You are building a Restaurant Management System. As a part of this system, you need to build a functionality using which orders placed by a customer can be tracked.

**Requirements:**
<ol>
  <li>Generally a customer places multiple orders before requesting for the bill. Hence, our system must be able to track orders placed by a customer.</li>
  <ul>
    <li>We should have an entity called as CustomerSession in our system which will help us track the orders placed by a customer.</li>
    <li>Once the customer places their 1st order, we should create a CustomerSession for them with status as ACTIVE.</li>
    <li>All the subsequent orders placed by the customer should be associated with the CustomerSession created for them.</li>
  </ul>
  <li>The request for placing an order will contain the user id of the customer and a Map, where the key will be menu item id and value is the quantity of the menu item ordered.</li>
  <li>This functionality should store the order details in the database.</li>
  <li>If the order is placed for a customer who is not present in the database, then we should throw an error.</li>
  <li>If an order contains a menu item which is not present in the database, then we should throw an error.</li>
  <li>Return the order details in the response.</li>
</ol>

**Instructions:**
<ul>
  <li>Refer the placeOrder method inside OrderController class.</li>
  <li>Refer the PlaceOrderRequestDto and PlaceOrderResponseDto for understanding the expected input and output to the functionality.</li>
  <li>Refer the models package to understand the models.</li>
  <li>Implement the OrderService, CustomerSessionRepository, UserRepository, MenuItemRepository and OrderRepository interfaces to achieve the above requirements.</li>
  <li>We need in memory database implementation for this assignment.</li>
  <li>Refer the TestOrderController class to understand the test cases that will be used to evaluate your solution.</li>
  <li>Do not modify existing methods and their parameters for interfaces, feel free to add more methods if required.</li>
</ul>
