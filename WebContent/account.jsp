<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Customer" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Order" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Account</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">

    <%@ include file="logout.jsp" %>

    <h1>My Account</h1>

    <%
        String accountMsg = (String) session.getAttribute("accountMsg");
        if (accountMsg != null) {
    %>
        <div style="
            margin: 10px 0;
            padding: 10px 12px;
            border: 1px solid #e31837;
            background: #fdeaea;
            color: #a40000;
            border-radius: 6px;
            font-weight: bold;
        ">
            <%= accountMsg %>
        </div>
    <%
            session.removeAttribute("accountMsg"); // show once
        }
    %>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <p style="color:red;"><%= error %></p>
    <%
        }

        Customer c = (Customer) request.getAttribute("customer");
        if (c == null) c = (Customer) session.getAttribute("currentCustomer");
        if (c == null) {
    %>
        <p style="color:red; font-weight:bold;">No customer is loaded. Please log in again.</p>
    <%
        } else {
    %>

    <form action="<%= request.getContextPath() %>/account" method="post">

        <h3>Basic Info</h3>

        <label>First Name:</label><br>
        <input type="text" name="firstName" value="<%= c.getFirstName() == null ? "" : c.getFirstName() %>" required><br><br>

        <label>Last Name:</label><br>
        <input type="text" name="lastName" value="<%= c.getLastName() == null ? "" : c.getLastName() %>" required><br><br>

        <label>Email (read-only):</label><br>
        <input type="email" value="<%= c.getEmail() == null ? "" : c.getEmail() %>" readonly><br><br>

        <hr style="margin:18px 0;">

        <h3>Payment Info</h3>

        <label>Credit Card Number:</label><br>
        <input type="text" name="creditCardNumber" value="<%= c.getCreditCardNumber() == null ? "" : c.getCreditCardNumber() %>"><br><br>

        <label>Expiry (MM/YY):</label><br>
        <input type="text" name="creditCardExpiry" value="<%= c.getCreditCardExpiry() == null ? "" : c.getCreditCardExpiry() %>"><br><br>

        <label>CVV:</label><br>
        <input type="text" name="creditCardCVV" value="<%= c.getCreditCardCVV() == null ? "" : c.getCreditCardCVV() %>"><br><br>

        <hr style="margin:18px 0;">

        <h3>Billing Address</h3>

        <label>Billing Address:</label><br>
        <input type="text" name="billingAddress" value="<%= c.getBillingAddress() == null ? "" : c.getBillingAddress() %>"><br><br>

        <label>Billing City:</label><br>
        <input type="text" name="billingCity" value="<%= c.getBillingCity() == null ? "" : c.getBillingCity() %>"><br><br>

        <label>Billing Postal Code:</label><br>
        <input type="text" name="billingPostal" value="<%= c.getBillingPostal() == null ? "" : c.getBillingPostal() %>"><br><br>

        <hr style="margin:18px 0;">

        <h3>Shipping Address</h3>

        <label>Shipping Address:</label><br>
        <input type="text" name="shippingAddress" value="<%= c.getShippingAddress() == null ? "" : c.getShippingAddress() %>"><br><br>

        <label>Shipping City:</label><br>
        <input type="text" name="shippingCity" value="<%= c.getShippingCity() == null ? "" : c.getShippingCity() %>"><br><br>

        <label>Shipping Postal Code:</label><br>
        <input type="text" name="shippingPostal" value="<%= c.getShippingPostal() == null ? "" : c.getShippingPostal() %>"><br><br>

        <button type="submit" class="btn">Save Changes</button>
    </form>

    <hr style="margin:25px 0;">

    <h2>Purchase History</h2>

    <%
        List<Order> orders = (List<Order>) request.getAttribute("orders");
        if (orders == null || orders.isEmpty()) {
    %>
        <p>No purchases yet.</p>
    <%
        } else {
    %>
        <table>
            <tr>
                <th>Order #</th>
                <th>Date</th>
                <th>Total</th>
            </tr>
            <%
                for (Order o : orders) {
            %>
            <tr>
                <td><%= o.getId() %></td>
                <td><%= o.getOrderDate() %></td>
                <td>$<%= String.format("%.2f", o.getTotal()) %></td>
            </tr>
            <%
                }
            %>
        </table>
    <%
        }
    %>

    <p style="margin-top:20px;">
        <a class="btn" href="<%= request.getContextPath() %>/catalog?view=all">Back to Catalog</a>
    </p>

    <%
        } // end else customer not null
    %>

</div>
</body>
</html>

